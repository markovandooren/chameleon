package org.aikodi.chameleon.oo.type;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.DeclarationContainerRelation;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Lists;

/**
 * An interface for classes and interfaces in object-oriented languages.
 * 
 * @author Marko van Dooren
 */
public interface Type extends DeclarationContainer, DeclarationWithType {

  public default boolean newSubtypeOf(Type other) throws LookupException {
    return sameAs(other);
  }
  
  @Override
  default List<? extends DeclarationContainerRelation> relations() throws LookupException {
  	return inheritanceRelations();
  }

  public default void accumulateSuperTypeJudge(SuperTypeJudge judge) throws LookupException {
    judge.add(this);
    List<Type> temp = getProperDirectSuperTypes();
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

  public List<Declaration> getIntroducedMembers();

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
  public void add(Declarator element) throws ChameleonProgrammerException;

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
  public void remove(Declarator element) throws ChameleonProgrammerException;

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
  public default void addAll(Collection<? extends Declarator> elements) {
  	elements.forEach(e -> add(e));
  }

  /**************
   * SUPERTYPES *
   **************/

 /**
  * Return the proper direct super types of this type. A proper super type is a super type
  * that is not equal to this type. A direct super type is a super type that is specified
  * by an inheritance relation of this type, or this type.
  * 
  * @return A list containing the direct super types of this type.
  * @throws LookupException The type of an inheritance relation could not be resolved.
  */
 public default List<Type> getProperDirectSuperTypes() throws LookupException {
		List<Type> result = Lists.create();
		for(InheritanceRelation element:inheritanceRelations()) {
			Type type = element.superType();
			if (type!=null) {
				result.add(type);
			}
		}
		return result;
	}

  public Set<Type> getAllSuperTypes() throws LookupException;

  public default boolean contains(Type other, TypeFixer trace) throws LookupException {
    return sameAs(other, trace);
  }
  
  public default boolean subtypeOf(Type other) throws LookupException {
    return subtypeOf(other, new TypeFixer());
  }

  public default boolean subtypeOf(Type other, TypeFixer trace) throws LookupException {
    TypeFixer clone = trace.clone();
    boolean result = sameAs(other,clone);
    if(! result) {
      clone = trace.clone();
      result = uniSubtypeOf(other,clone);
      if(! result) {
        result = other.uniSupertypeOf(this, trace);
      }
    }
    return result;
    //		return sameAs(other,trace.clone()) || uniSubtypeOf(other,trace.clone()) || other.uniSupertypeOf(this, trace.clone());
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
  public <T extends Declaration> List<T> localMembers(final Class<T> kind) throws LookupException;

  /**
   * Return the members that are implicitly part of this type, such as default constructors and destructors.
   * @return
   */
  public List<Declaration> implicitMembers();

  public <M extends Declaration> List<M> implicitMembers(Class<M> kind);

  /**
   * Return the members directly declared by this type. The order of the elements in the list is the order in which they
   * are written in the type.
   * @return
   * @throws LookupException 
   */
  public List<Declaration> localMembers() throws LookupException;

  public <T extends Declaration> List<T> directlyDeclaredMembers(Class<T> kind);

  public <T extends Declaration> List<T> directlyDeclaredMembers(Class<T> kind, ChameleonProperty property);

  public List<Declaration> directlyDeclaredMembers();

  public <D extends Declaration> List<SelectionResult<D>> members(DeclarationSelector<D> selector) throws LookupException;

  @SuppressWarnings("unchecked")
  public <D extends Declaration> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException;

  public List<Declaration> members() throws LookupException;

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
  public <M extends Declaration> List<M> members(final Class<M> kind) throws LookupException;

  /**
   * DO NOT CONFUSE THIS METHOD WITH localMembers. This method does not
   * transform type elements into members.
   * 
   * FIXME: rename to localDeclarators()
   * 
   * @return
   */
  public List<? extends Declarator> directlyDeclaredElements();

  public <T extends Declarator> List<T> directlyDeclaredElements(Class<T> kind);

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

  public void replace(Declarator oldElement, Declarator newElement);

  public Type baseType();

  //	static ThreadLocal<StackOverflowTracer> tracer = new ThreadLocal<StackOverflowTracer>() {
  //	  protected StackOverflowTracer initialValue() {
  //	    return new StackOverflowTracer(5);
  //	   }
  //	  };

  public default boolean compatibleParameters(Type other, TypeFixer trace) throws LookupException {
    //	  tracer.get().push();
    //		second.toString();
    int size = nbTypeParameters(TypeParameter.class);
    boolean result = true;
    for(int i=0; i< size && result;i++) {
      TypeParameter otherParameter = other.parameter(TypeParameter.class, i);
      TypeParameter myParameter = parameter(TypeParameter.class,i);
      result = otherParameter.contains(myParameter, trace.clone());
    }
    return result;
//    List<TypeParameter> myParameters = parameters(TypeParameter.class);
//    List<TypeParameter> otherParameters = second.parameters(TypeParameter.class);
//    final boolean forAll = forAll(myParameters, otherParameters, (f,s) -> s.contains(f, trace));
//    //    tracer.get().pop();
//    return forAll;
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
    boolean result = uniSameAs(other,newTrace);
    if(! result) {
      newTrace = trace.clone();
      newTrace.add(other, this);
      result = other.uniSameAs(this,newTrace);
    }
    return result;
  }

  public boolean uniSameAs(Type aliasedType, TypeFixer trace) throws LookupException;

  public default Type lowerBound() throws LookupException {
    return this;
  }

  public default Type upperBound() throws LookupException {
    return this;
  }

//  public <D extends Declaration> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException;
//
//  public <D extends Declaration> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException;
//
//  public <D extends Declaration> List<D> membersDirectlyAliasing(MemberRelationSelector<D> selector) throws LookupException;

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
