package chameleon.oo.type;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.member.MemberRelationSelector;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Pair;

public interface Type extends NamespaceOrType<Type,SimpleNameSignature,Type>, 
VariableOrType<Type,SimpleNameSignature,Type>, 
Definition<Type,SimpleNameSignature,Type>,
Cloneable, 
ExceptionSource<Type>, 
DeclarationContainer<Type>,
DeclarationWithType<Type,SimpleNameSignature,Type>, Member<Type,SimpleNameSignature,Type>, 
AspectOrType<Type,Type> {

	public Class<SimpleNameSignature> signatureType();

	public Type declarationType();

	public void flushLocalCache();

	public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException;
	
	/********
	 * NAME *
	 ********/

	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	public String getName();

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

	public LookupStrategy targetContext() throws LookupException;

	public LookupStrategy localStrategy() throws LookupException;

	/**
	 * If the given element is an inheritance relation, the lookup must proceed to the parent. For other elements,
	 * the context is a lexical context connected to the target context to perform a local search.
	 * @throws LookupException 
	 */
	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException;

	public List<ParameterBlock> parameterBlocks();
	
	public <P extends Parameter> ParameterBlock<?,P> parameterBlock(Class<P> kind);
	
	public void addParameterBlock(ParameterBlock block);
	
	public void removeParameterBlock(ParameterBlock block);
	
	public Class<? extends Parameter> kindOf(ParameterBlock block) throws LookupException;
	
	public <P extends Parameter> List<P> parameters(Class<P> kind);

	/**
	 * Indices start at 1.
	 */
	public <P extends Parameter> P parameter(Class<P> kind, int index);

	public <P extends Parameter> int nbTypeParameters(Class<P> kind);

	public <P extends Parameter> void addParameter(Class<P> kind,P parameter);

	public <P extends Parameter> void replaceParameter(Class<P> kind, P oldParameter, P newParameter);

	public <P extends Parameter> void replaceAllParameter(Class<P> kind, List<P> newParameters);

	/************************
	 * BEING A TYPE ELEMENT *
	 ************************/

	public List<Member> getIntroducedMembers();

	public Ternary complete();

	/**********
	 * ACCESS *
	 **********/

	public Type getType();

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

	public boolean subTypeOf(Type other) throws LookupException;

	/**
	 * Check if this type equals the given other type. This is
	 * a unidirectional check to keep things extensible. It is fine
	 * if equals(other) is false, but other.equals(this) is true.
	 *  
	 * @param other
	 * @return
	 */
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
	 * @throws LookupException 
	 */
	/*@
	  @ public behavior
	  @
	  @ post \result != null;
	  @*/
	public List<InheritanceRelation> inheritanceRelations() throws LookupException;
	
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
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;

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

	public List<Member> directlyDeclaredMembers();

	public <D extends Member> List<D> members(DeclarationSelector<D> selector) throws LookupException;

	@SuppressWarnings("unchecked")
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException;

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

	/****************
	 * CONSTRUCTORS *
	 ****************/

	public Type clone();

	/*@
	 @ also public behavior
	 @
	 @ post \result.containsAll(getSuperTypeReferences());
	 @ post \result.containsAll(getMembers());
	 @ post \result.containsAll(getModifiers());
	 @*/
	public List<Element> children();

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

	public CheckedExceptionList getCEL() throws LookupException;

	public CheckedExceptionList getAbsCEL() throws LookupException;

	public List<? extends Declaration> declarations() throws LookupException;

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException;

	public Type alias(SimpleNameSignature sig);

	public Type intersection(Type type) throws LookupException;
	
	public Type intersectionDoubleDispatch(Type type) throws LookupException;
	
	public Type intersectionDoubleDispatch(IntersectionType type) throws LookupException;

	public void replace(TypeElement oldElement, TypeElement newElement);

	public Type baseType();

	public VerificationResult verifySelf();

	public boolean upperBoundNotHigherThan(Type other, List<Pair<Type, TypeParameter>> trace) throws LookupException;

	public Type union(Type lowerBound) throws LookupException;
	
	public Type unionDoubleDispatch(Type type) throws LookupException;
	
	public Type unionDoubleDispatch(UnionType type) throws LookupException;

	public boolean sameAs(Type aliasedType, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException;

	public boolean uniSameAs(Type aliasedType, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException;
	
	public Type lowerBound() throws LookupException;
	
	public Type upperBound() throws LookupException;

	public <D extends Member> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException;
	
	public <D extends Member> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException;
	
	public <D extends Member> List<D> membersDirectlyAliasing(MemberRelationSelector<D> selector) throws LookupException;
}