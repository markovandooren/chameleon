package org.aikodi.chameleon.core.declaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.language.LanguageImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.member.DeclarationComparator;
//import org.aikodi.chameleon.oo.member.HidesRelation;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.exception.Handler;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

/**
 * <p>
 * A declaration is an element that can be referenced from other parts of the
 * code. Examples include types, methods, and variables.
 * </p>
 * 
 * <p>
 * Each declaration has a {@link Signature}, which is used to identify that
 * declaration. Information that is not used for identification, such as the
 * return type of a method, does not belong in the signature. Therefore, it can
 * be that a specific declaration has a signature that is derived. A Method, for
 * example, has a MethodHeader which contains all the information that is
 * required to compute the signature.
 * </p>
 * 
 * <p>Each declaration results in the lookup of an actual declaration of type D.
 * This type ensures that if a declaration is just a stub, such as a generic
 * parameter, the transformation performed by the resolveForResult method
 * returns a declaration of the same family. In case of a type parameter, this
 * ensures that the stub type will be transformed into a type.</p>
 * 
 * <embed src="declaration-object.svg"/>
 * 
 * <h3>Events</h3>
 * 
 * <p>When the {@link #name()} of a declaration changes, it sends a {@link NameChanged}
 * event. Note, however, that the event can be sent by either the declaration
 * itself, or its {@link #signature()}. This allows lazy instantiation of signatures.</p>
 * 
 * @author Marko van Dooren
 */
/*
@startuml declaration-object.svg
interface Element
interface SelectionResult
interface Signature
interface Declaration
Element <|-- Declaration 
SelectionResult <|-- Declaration
Declaration -- Signature
@enduml
 */
public interface Declaration extends Element, SelectionResult, DeclarationContainer, ElementWithModifiers, Declarator {//

  /**
   * @return the signature of this declaration. The signature represents the identity of this declaration.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Signature signature();

  /**
   * @return the name of a declaration is the name of its signature.
   */
 /*@
   @ public behavior
   @
   @ post \result == signature().name();
   @*/
  public default String name() {
    return signature().name();
  }
  
  /**
   * Change the signature of this declaration to the given declaration.
   * 
   * @param signature The new signature of this declaration
   */
 /*@
   @ public behavior
   @
   @ post signature() == signature;
   @*/
  public void setSignature(Signature signature);

  /**
   * Change the name of the signature of this declaration.
   * @param name
   */
 /*@
   @ public behavior
   @
   @ post signature().name().equals(name); 
   @*/
  public default void setName(String name) {
    signature().setName(name);
  }
  
  /**
   * <p>
   * Return the declaration that is used for selection
   * </p>
   * <p>
   * Because some declarations, such as type parameters, are variables for other
   * declarations, the lookup process invokes selectionDeclaration() on a
   * declaration before giving it to the selection method of a
   * {@link DeclarationSelector}. A type parameter, for example, will return a
   * type with the same name as itself, which behaves like the upper bound of
   * its type constraints with respect to lookup, yet has its own unique
   * identity. By creating a type with the same name as the type parameter, the
   * DeclarationSelector for types does not have to know about the existence of
   * type parameters.</p>
   * 
   * <p>In most cases this method simply returns the current object. This
   * is also the default behavior.</p>
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default Declaration selectionDeclaration() throws LookupException {
    return this;
  }
  

  /**
   * As explained in the selectionDeclaration method, formal generic parameters
   * create stub types for matching. In case of an instantiated generic
   * parameter, though, the end result of a lookup should be the actual type
   * argument, and not a stub. Therefore, the resolveForResult method performs a
   * final transformation. In case of a stub of an instantiated generic
   * parameter, the actual type that is used as an argument is returned.
   * 
   * @throws LookupException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default Declaration actualDeclaration() throws LookupException {
    return this;
  }
  
  /**
   * Return the declaration that declared this declaration. In most cases the
   * declaration is the same as the declarator. But for example, for variables
   * in Java, a single variable declarator can declare multiple variables.
   * Becase the model must represent the lexical program, Java variables and
   * variable declarators are separate objects.
   * 
   * @return The declaration that declared this declaration.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default Declaration declarator() {
    return this;
  }
  
  /**
   * Return the scope of this declaration. The scope of a declaration denotes
   * the regions of the program in which the declaration is visible.
   * 
   * @throws ModelException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default Scope scope() throws ModelException {
    Scope result = null;
    ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
    if(scopeProperty instanceof ScopeProperty) {
      result = ((ScopeProperty)scopeProperty).scope(this);
    } else if(scopeProperty != null){
      throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
    }
    return result;
  }
 
  /**
   * <p>Check whether this declaration is complete (whether all necessary elements
   * are present). A complete declaration <b>can</b> be non-abstract or
   * abstract. An incomplete declaration, however, must always be abstract.
   * Defined (and thus its inverse: abstract) is a dynamic property that uses
   * this method to determine whether or not it applies to this declaration.</p>
   * 
   * <p>The default implementation returns true.</p>
   * 
   * @throws LookupException
   */
  public default boolean complete() throws LookupException {
    return true;
  }

  /**
   * <p>
   * Return the target context of this target.
   * </p>
   *
   * <p>
   * A target context is the context used to look up elements that are expressed
   * relative to a target. For example, when looking up <code>a.b</code>, first
   * <code>a</code> is looked up in the current context. After that,
   * <code>b</code> must be looked up in the context of the element returned by
   * the lookup of <code>a</code>. But <code>b</code> must <b>not</b> be lookup
   * up as if it were used in the lexical context of the class representing the
   * type of <code>a</code>. Therefore, two contexts are provided: a lexical
   * context and a target context.
   * </p>
   *
   * <p>
   * A few examples in the language Java:
   * </p>
   * <ol>
   * <li>in "expr.f", "f" must be looked up in the static type of "expr", and
   * not in its lexical context, which is the current lexical context.</li>
   * <li>in "typename.f", "f" must be looked up in the type represented by
   * "typename"</li>
   * <li>in "packagename.f", "f" must be looked up in the package represented by
   * "package"</li>
   * </ol>
   */
  public default LocalLookupContext<?> targetContext() throws LookupException {
  	return language().lookupFactory().createTargetLookupStrategy(this);
  }
  
  /**
   * Check whether the given declaration has the same signature as this declaration.
   * 
   * @param declaration The declaration of which must be checked whether it has
   * the same signature as this one.
   * @return True if the given declaration has the same signature as this one,
   * false otherwise.
   * @throws LookupException
   */
  public default boolean sameSignatureAs(Declaration declaration) throws LookupException {
    return signature().sameAs(declaration.signature());
  }
  
  /**
   * <p>Return all cross references in the model that reference this declaration.
   * This can be an expensive operation on a model whose references are not 
   * yet cached.</p>
   * 
   * <p>The handler determines what will happen when a cross-reference throws a
   * LookupException. Handler.fail(LookupException.class) will cause the
   * search to stop immediately. {@link Handler#resume()} will
   * ignore the exception and cause the search to continue. If you provide
   * a handler for an exception that is no lookup exception, the effect
   * is the same as using {@link Handler#resume()}.</p>
   * 
   * @param handler The strategy that must be used when an exception is thrown
   *  while search the cross-references.
   * @return all cross references in the model that reference this declaration.
   * @throws LookupException A cross-reference could not be resolved.
   */
 /*@
   @ public behavior
   @
   @ post result != null
   @ post result.stream().allMatch(cref -> cref.getElement().sameAs(this))
   @*/
  public default <E extends Exception> List<CrossReference<?>> findAllReferences(Handler<E> handler) throws E {
     List<CrossReference<?>> result = new ArrayList<>();
     root().apply(new Action<CrossReference, E>(CrossReference.class) {

        @Override
        protected void doPerform(CrossReference crossReference) throws E {
           try {
              if(crossReference.getElement().sameAs(Declaration.this)) {
                result.add(crossReference); 
              }
           } catch (LookupException e) {
              handler.handle(e);
           };
        }
     });
     return result;
  }

  /**
   * By default returns the current object.
   */
  @Override
  public default Declaration finalDeclaration() {
    return this;
  }

  /**
   * By default, this returns the {@link #finalDeclaration()}.
   */
  @Override
  public default Declaration template() {
    return finalDeclaration();
  }

  @Override
  public default SelectionResult updatedTo(Declaration declaration) {
    return declaration;
  }

  @Override
  public default List<Declaration> declaredDeclarations() {
  	return Lists.create(this);
  }
  
  /**
   * Check whether this declaration can override the given other declaration.
   * 
   * @param other
   * @return
   * @throws LookupException
   */
  public default boolean compatibleSignature(Declaration other) throws LookupException {
  	notNull(other);
  	return signature().sameAs(other.signature());
  }
  
  /**
   * Return the members that are overridden by this member.
   * 
   * @return A non-null set containing the members that are overridden by this member, 
   * and that are either lexical members of the model, or generated members that are reachable 
   * via this member. Generation of elements can caused a theoretically infinite number of members
   * to be overridden by this member. Only a few of them will actually exist as objects at any time,
   * and it would be too expensive to track them given the fact that the result would be incomplete.
   * As such, the result is limited to those members that are typically used by tools.
   *  
   * @throws LookupException
   */
  public default Set<? extends Declaration> overriddenDeclarations() throws LookupException {
    Set<Declaration> result = null;
    if(result == null) {
      List<Declaration> todo = new ArrayList<Declaration>(directlyOverriddenDeclarations());
      Map<DeclarationContainer,List<Declaration>> visitedContainers = new HashMap<>();
      while(! todo.isEmpty()) {
      	Declaration current = todo.get(0);
        todo.remove(0);
        DeclarationContainer container = current.nearestAncestor(DeclarationContainer.class);
        if(! visitedContainers.containsKey(container)) {
          visitedContainers.put(container, Lists.<Declaration>create());
        }
        List<Declaration> done = visitedContainers.get(container);
        boolean contains = false;
        for(Declaration declaration:done) {
          if(declaration.sameSignatureAs(current)) {
            contains = true;
            break;
          }
        }
        if(! contains) {
          done.add(current);
          todo.addAll(current.directlyOverriddenDeclarations());
          todo.addAll(current.aliasedDeclarations());
          todo.addAll(current.aliasingDeclarations());
        }
      }
      result = new HashSet<Declaration>();
      for(List<Declaration> members: visitedContainers.values()) {
        result.addAll(members);
      }
    }
    return result;
  }

  /**
   * @return The <em>reachable</em> declaration that are aliased by this declaration.
   *         Note that for e.g. type constructor instantiations (List&lt;A&gt;) there
   *         can be multiple equal instances. Declarations of the other instances are
   *         considered unreachable. This method cannot find every theoretical
   *         match because the object does not know all of its 'twins'.
   * @throws LookupException
   */
  public default Set<? extends Declaration> aliasedDeclarations() throws LookupException {
    List<Declaration> todo = new ArrayList<>(directlyAliasedDeclarations());
    Set<Declaration> result = new HashSet<Declaration>();
    while(! todo.isEmpty()) {
    	Declaration m = todo.get(0);
      todo.remove(0);
      if(result.add(m)) {
        todo.addAll(m.directlyAliasedDeclarations());
      }
    }
    return result;
  }

  /**
   * @return The <em>reachable</em> declarations that are an alias of this declaration.
   *         Note that for type constructor instantiations (List&lt;A&gt;) there
   *         can be multiple equal instances. Declarations of the other instances are
   *         considered unreachable. This method cannot find every theoretical
   *         match because the object does not know all of its 'twins'.
   * @throws LookupException
   */
  public default Set<? extends Declaration> aliasingDeclarations() throws LookupException {
    List<Declaration> todo = new ArrayList<>(directlyAliasingDeclarations());
    Set<Declaration> result = new HashSet<Declaration>();
    while(! todo.isEmpty()) {
    	Declaration m = todo.get(0);
      todo.remove(0);
      if(result.add(m)) {
        todo.addAll(m.directlyAliasingDeclarations());
      }
    }
    return result;
  }

  public default Set<Declaration> directlyOverriddenDeclarations() throws LookupException {
  	Set<Declaration> result = new HashSet<>();
  	//FIXME This may have to be determined by the language.
  	DeclarationRelation relation = overridesRelation(); 
		nearestAncestor(DeclarationContainer.class).directlyOverriddenDeclarations(this, relation, result);
  	return result;
  }

  /**
   * Return a relation that determines when a given declaration overrides another one.
   * 
   * FIXME: this may have to be be redirected to the language.
   * @return
   */
	public default DeclarationRelation overridesRelation() {
		return new DeclarationRelation(true){
			@Override
			public boolean contains(Declaration overriding, Declaration overridden) throws LookupException {
	       return overridden.isTrue(overridden.language(LanguageImpl.class).OVERRIDABLE)
			&& overridden.compatibleSignature(overriding);
			}
		};
	}

  public default Set<Declaration> directlyAliasedDeclarations() throws LookupException {
  	Set<Declaration> result = new HashSet<>();
		nearestAncestor(DeclarationContainer.class).directlyAliasedDeclarations(this, result);
  	return result;
  }

  public default Set<Declaration> directlyAliasingDeclarations() throws LookupException {
  	Set<Declaration> result = new HashSet<>();
		nearestAncestor(DeclarationContainer.class).directlyAliasedDeclarations(this, result);
  	return result;
  }

	/**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == false;
   @*/
  public default boolean hides(Declaration other) throws LookupException {
    return new DeclarationComparator<Declaration>(Declaration.class).contains(this,other);
  }

}
