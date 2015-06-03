package org.aikodi.chameleon.oo.type;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.findFirst;
import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.forAll;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.member.MemberRelationSelector;
import org.aikodi.chameleon.oo.type.generics.ConstrainedType;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.StackOverflowTracer;
import org.aikodi.chameleon.util.Util;

import com.google.common.collect.ImmutableList;

/**
 * An interface for classes and interfaces in object-oriented languages.
 * 
 * @author Marko van Dooren
 */
public interface Type extends DeclarationContainer, DeclarationWithType, Member {

	public default boolean newSubtypeOf(Type other) throws LookupException {
		return sameAs(other);
	}

	public static class SuperTypeJudge {
		// Nasty internal structure to prevent the creation
		// of a lot of lists with just a single element.
		private Map<String,Object> _map = new HashMap<>();

		void add(Type type) throws LookupException {
			Object o = _map.get(type.name());
			if(o == null) {
				_map.put(type.name(), type);
			} else if(o instanceof List) {
				_map.put(type.name(),ImmutableList.builder().add(type).addAll((List)o).build());
			} else {
				//Check for duplicates
				if(! ((Type)o).baseType().sameAs(type.baseType())) {
					_map.put(type.name(), ImmutableList.builder().add(type).add(o).build());
				}
			}
		}

		Set<Type> types() {
			Set<Type> result = new HashSet<>();
			for(Object o: _map.values()) {
				if(o instanceof List) {
					result.addAll((List)o);
				} else {
					result.add((Type) o);
				}
			}
			return result;
		}

		public Type get(Type baseType) throws LookupException {
			final Type realBase = baseType.baseType();
			Object o = _map.get(realBase.name());
			if(o instanceof List) {
				return findFirst((List<Type>) o, t -> t.baseType().sameAs(realBase));
			} else {
				Type stored = (Type)o;
				return stored == null ? null : (stored.baseType().sameAs(realBase) ? stored : null);
			}
		}

		void merge(SuperTypeJudge superJudge) throws LookupException {
			for(Object v :superJudge._map.values()) {
				if(v instanceof List) {
					for(Type t: (List<Type>)v) {
						add(t);
					}
				} else {
					add((Type)v);
				}
			}
		}
	}

	public default void accumulateSuperTypeJudge(SuperTypeJudge judge) throws LookupException {
		judge.add(this);
		List<Type> temp = getDirectSuperTypes();
		for(Type type:temp) {
			Type existing = judge.get(type);
			if(existing == null) {
				type.accumulateSuperTypeJudge(judge);
			}
		}
	}


	/**
	 * Find the super type with the same base type as the given type.
	 * 
	 * @param type The type with the same base type as the requested super type.
	 * @return A super type of this type that has the same base type as the given
	 *         type. If there is no such super type, null is returned.
	 * @throws LookupException
	 */
	public default Type getSuperType(Type type) throws LookupException {
		return superTypeJudge().get(type);
	}

	public SuperTypeJudge superTypeJudge() throws LookupException;

	public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException;

	public void newAccumulateAllSuperTypes(Set<Type> acc) throws LookupException;

	public void newAccumulateSelfAndAllSuperTypes(Set<Type> acc) throws LookupException;


	public Set<Type> getSelfAndAllSuperTypesView() throws LookupException;

	public abstract List<InheritanceRelation> explicitNonMemberInheritanceRelations();

	public <I extends InheritanceRelation> List<I> explicitNonMemberInheritanceRelations(Class<I> kind);

	public List<InheritanceRelation> implicitNonMemberInheritanceRelations();

	public default void reactOnDescendantAdded(Element element) {}

	public default void reactOnDescendantRemoved(Element element) {}

	public default void reactOnDescendantReplaced(Element oldElement, Element newElement) {}

	/**
	 * Return the fully qualified name.
	 * @throws LookupException 
	 */
	/*@
	  @ public behavior
	  @
	  @ getPackage().getFullyQualifiedName().equals("") ==> \result == getName();
	  @ ! getPackage().getFullyQualifiedName().equals("") == > \result.equals(getPackage().getFullyQualifiedName() + getName());
	  @*/
	public String getFullyQualifiedName();

	/*******************
	 * LEXICAL CONTEXT 
	 *******************/

	@Override
	public LocalLookupContext<?> targetContext() throws LookupException;

	@Override
	public LookupContext localContext() throws LookupException;

	/**
	 * If the given element is an inheritance relation, the lookup must proceed to the parent. For other elements,
	 * the context is a lexical context connected to the target context to perform a local search.
	 * @throws LookupException 
	 */
	@Override
	public LookupContext lookupContext(Element element) throws LookupException;

	public List<ParameterBlock> parameterBlocks();

	public <P extends Parameter> ParameterBlock<P> parameterBlock(Class<P> kind);

	public void addParameterBlock(ParameterBlock block);

	public void removeParameterBlock(ParameterBlock block);

	public <P extends Parameter> List<P> parameters(Class<P> kind);

	/**
	 * Indices start at 1.
	 */
	public <P extends Parameter> P parameter(Class<P> kind, int index);

	public <P extends Parameter> int nbTypeParameters(Class<P> kind);

	public <P extends Parameter> void addParameter(Class<P> kind,P parameter);

	public <P extends Parameter> void addAllParameters(Class<P> kind,Collection<P> parameter);

	public <P extends Parameter> void replaceParameter(Class<P> kind, P oldParameter, P newParameter);

	public <P extends Parameter> void replaceAllParameters(Class<P> kind, List<P> newParameters);

	/************************
	 * BEING A TYPE ELEMENT *
	 ************************/

	@Override
	public List<Member> getIntroducedMembers();

	/**********
	 * ACCESS *
	 **********/

	/**
	 * Add the given element to this type.
	 * 
	 * @throws ChameleonProgrammerException
	 *         The given element could not be added. E.g when you try to add
	 *         an element to a computed type.
	 */
	/*@
	  @ public behavior
	  @
	  @ pre element != null;
	  @
	  @ post directlyDeclaredElements().contains(element);
	  @*/
	public void add(TypeElement element) throws ChameleonProgrammerException;

	/**
	 * Remove the given element to this type.
	 * 
	 * @throws ChameleonProgrammerException
	 *         The given element could not be added. E.g when you try to add
	 *         an element to a computed type.
	 */
	/*@
	  @ public behavior
	  @
	  @ pre element != null;
	  @
	  @ post ! directlyDeclaredElements().contains(element);
	  @*/
	public void remove(TypeElement element) throws ChameleonProgrammerException;

	/**
	 * Add all type elements in the given collection to this type.
	 * @param elements
	 * @throws ChameleonProgrammerException
	 */
	/*@
	  @ public behavior
	  @
	  @ pre elements != null;
	  @ pre !elements.contains(null);
	  @
	  @ post directlyDeclaredElements().containsAll(elements);
	  @*/
	public void addAll(Collection<? extends TypeElement> elements) throws ChameleonProgrammerException;

	/**************
	 * SUPERTYPES *
	 **************/

	public List<Type> getDirectSuperTypes() throws LookupException;

	public List<Type> getDirectSuperClasses() throws LookupException;

	public Set<Type> getAllSuperTypes() throws LookupException;

	public default boolean subtypeOf(Type other) throws LookupException {
		return subtypeOf(other, new TypeFixer());
	}

	public default boolean subtypeOf(Type other, TypeFixer trace) throws LookupException {
		return sameAs(other,trace.clone()) || uniSubtypeOf(other,trace.clone()) || other.uniSupertypeOf(this, trace.clone());
	}

	public default boolean uniSupertypeOf(Type type, TypeFixer trace) throws LookupException {
		return false;
	}

	public default boolean uniSubtypeOf(Type other, TypeFixer trace) throws LookupException {
		Type sameBase = getSuperType(other);
		return sameBase != null && sameBase.compatibleParameters(other, trace);
	}


	
	/**
	 * Check if this type equals the given other type. This is
	 * a unidirectional check to keep things extensible. It is fine
	 * if equals(other) is false, but other.equals(this) is true.
	 *  
	 * @param other
	 * @return
	 */
	@Override
	public boolean uniSameAs(Element other) throws LookupException;

	/**
	 * Check if this type is assignable to another type.
	 * 
	 * @param other
	 * @return
	 * @throws LookupException
	 */
	/*@
	  @ public behavior
	  @
	  @ post \result == equals(other) || subTypeOf(other);
	  @*/
	public boolean assignableTo(Type other) throws LookupException;

	/**
	 * Return the inheritance relations of this type.
	 * 
	 * @throws LookupException 
	 */
	/*@
	  @ public behavior
	  @
	  @ post \result != null;
	  @*/
	public default List<InheritanceRelation> inheritanceRelations() throws LookupException {
		return nonMemberInheritanceRelations();
	}

	public List<InheritanceRelation> nonMemberInheritanceRelations();

	public <I extends InheritanceRelation> List<I> nonMemberInheritanceRelations(Class<I> kind);

	/**
	 * Add the give given inheritance relation to this type.
	 * @param type
	 * @throws ChameleonProgrammerException
	 *         It is not possible to add the given type. E.g. you cannot
	 *         add an inheritance relation to a computed type.
	 */
	/*@
	  @ public behavior
	  @
	  @ pre relation != null;
	  @ post inheritanceRelations().contains(relation);
	  @*/
	public void addInheritanceRelation(InheritanceRelation relation);

	public void addAllInheritanceRelations(Collection<InheritanceRelation> relations);
	/**
	 * Remove the give given inheritance relation from this type.
	 * @param type
	 * @throws ChameleonProgrammerException
	 *         It is not possible to remove the given type. E.g. you cannot
	 *         remove an inheritance relation to a computed type.
	 */
	/*@
	  @ public behavior
	  @
	  @ pre relation != null;
	  @ post ! inheritanceRelations().contains(relation);
	  @*/
	public void removeNonMemberInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;

	public void removeAllNonMemberInheritanceRelations();

	/**
	 * Return the members of the given kind directly declared by this type.
	 * @return
	 * @throws LookupException 
	 * @throws  
	 */
	public <T extends Member> List<T> localMembers(final Class<T> kind) throws LookupException;

	/**
	 * Return the members that are implicitly part of this type, such as default constructors and destructors.
	 * @return
	 */
	public List<Member> implicitMembers();

	public <M extends Member> List<M> implicitMembers(Class<M> kind);

	/**
	 * Return the members directly declared by this type. The order of the elements in the list is the order in which they
	 * are written in the type.
	 * @return
	 * @throws LookupException 
	 */
	public List<Member> localMembers() throws LookupException;

	public <T extends Member> List<T> directlyDeclaredMembers(Class<T> kind);

	public <T extends Member> List<T> directlyDeclaredMembers(Class<T> kind, ChameleonProperty property);

	public List<Member> directlyDeclaredMembers();

	public <D extends Member> List<SelectionResult<D>> members(DeclarationSelector<D> selector) throws LookupException;

	@SuppressWarnings("unchecked")
	public <D extends Member> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException;

	public List<Member> members() throws LookupException;

	//    public <M extends Member> Set<M> potentiallyInheritedMembers(final Class<M> kind) throws MetamodelException {
	//  		final Set<M> result = new HashSet<M>();
	//			for (InheritanceRelation rel : inheritanceRelations()) {
	//				result.addAll(rel.potentiallyInheritedMembers(kind));
	//			}
	//  		return result;
	//    }
	//
	/**
	 * Return the members of this class.
	 * @param <M>
	 * @param kind
	 * @return
	 * @throws LookupException
	 */
	public <M extends Member> List<M> members(final Class<M> kind) throws LookupException;

	/**
	 * DO NOT CONFUSE THIS METHOD WITH localMembers. This method does not
	 * transform type elements into members.
	 * 
	 * @return
	 */
	public List<? extends TypeElement> directlyDeclaredElements();

	public <T extends TypeElement> List<T> directlyDeclaredElements(Class<T> kind);

	/********************
	 * EXCEPTION SOURCE *
	 ********************/

	//	public CheckedExceptionList getCEL() throws LookupException;
	//
	//	public CheckedExceptionList getAbsCEL() throws LookupException;

	@Override
	public List<? extends Declaration> declarations() throws LookupException;

	public Type alias(String name);

	public Type intersection(Type type) throws LookupException;

	public Type intersectionDoubleDispatch(Type type) throws LookupException;

	public Type intersectionDoubleDispatch(IntersectionType type) throws LookupException;

	public void replace(TypeElement oldElement, TypeElement newElement);

	public Type baseType();

	//	static ThreadLocal<StackOverflowTracer> tracer = new ThreadLocal<StackOverflowTracer>() {
	//	  protected StackOverflowTracer initialValue() {
	//	    return new StackOverflowTracer(5);
	//	   }
	//	  };

	public default boolean compatibleParameters(Type second, TypeFixer trace) throws LookupException {
		//	  tracer.get().push();
//		second.toString();
		final boolean forAll = forAll(parameters(TypeParameter.class), second.parameters(TypeParameter.class), (f,s) -> s.contains(f, trace));
		//    tracer.get().pop();
		return forAll;
	}


	public Type union(Type lowerBound) throws LookupException;

	public Type unionDoubleDispatch(Type type) throws LookupException;

	public Type unionDoubleDispatch(UnionType type) throws LookupException;

//	public default boolean sameInstanceAs(Type aliasedType, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
//		return sameAs(aliasedType, trace);
//	}


	public default boolean sameAs(Type other, TypeFixer trace) throws LookupException {
		if(other == this || trace.contains(other, this)) {
			return true;
		}
		TypeFixer newTrace = trace.clone();
		newTrace.add(other, this);
		return uniSameAs(other,newTrace) || other.uniSameAs(this,newTrace);
	}

	public boolean uniSameAs(Type aliasedType, TypeFixer trace) throws LookupException;

	public default Type lowerBound() throws LookupException {
		return this;
	}
	
	public default Type upperBound() throws LookupException {
		return this;
	}

	public default Type actualLowerBound() throws LookupException {
		return lowerBound();
	}

	public default Type actualUpperBound() throws LookupException {
		return upperBound();
	}

//	public default Type ultimateLowerBound() throws LookupException {
//		Type result = lowerBound();
//		//		boolean same = false;
//		//		while(! same) {
//		//			Type tmp = result.lowerBound();
//		//			if(tmp != result) {
//		//				result = tmp;
//		//				same = false;
//		//			} else {
//		//				same = true;
//		//			}
//		//		}
//		if(result != this) {
//			result = result.ultimateLowerBound();
//		}
//		return result;
//	}

//	public default Type ultimateUpperBound() throws LookupException {
//		//		Type result = upperBound();
//		//		boolean same = false;
//		//		while(! same) {
//		//			Type tmp = result.upperBound();
//		//			if(tmp != result) {
//		//				result = tmp;
//		//				same = false;
//		//			} else {
//		//				same = true;
//		//			}
//		//		}
//		//		return result;
//		Type result = upperBound();
//		if(result != this) {
//			result = result.ultimateUpperBound();
//		}
//		return result;
//	}

	public <D extends Member> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException;

	public <D extends Member> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException;

	public <D extends Member> List<D> membersDirectlyAliasing(MemberRelationSelector<D> selector) throws LookupException;

	public String infoName();

	/**
	 * Verify whether the this type is a subtype of the given other type. If that is the case, then a valid verification result is returned.
	 * Otherwise, a problem is reported. The message of the problem is constructed using the descriptions of the meaning of
	 * each type as determined by the arguments.
	 * 
	 * @param otherType
	 * @param meaningThisType
	 * @param meaningOtherType
	 * @return
	 */
	public Verification verifySubtypeOf(Type otherType, String meaningThisType, String meaningOtherType, Element cause);


	/**
	 * @return
	 */
	public default boolean isWildCard() {
		return false;
	}

}
