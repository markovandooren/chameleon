package org.aikodi.chameleon.core.declaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.exception.Handler;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * A declaration introduces an element that can be referenced from other parts of the code.
 * Examples include types, methods, and variables.
 * 
 * Each declaration has a signature, which is used to identify that declaration. Information
 * that is not used for identification, such as the return type of a method, does not belong in
 * the signature. Therefore, it can be that a specific declaration has a signature that is derived.
 * A Method, for example, has a MethodHeader which contains all the information that is required to
 * compute the signature.
 * 
 * Each declaration results in the lookup of an actual declaration of type D. This type ensures that 
 * if a declaration is just a stub, such as a generic parameter, the transformation performed by the resolveForResult 
 * method returns a declaration of the same family. In case of a type parameter, this ensures that the stub type will be transformed
 * into a type.
 * 
 * <embed src="declaration-object.svg"/>
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
public interface Declaration extends Element, SelectionResult {//

  /**
   * Return the signature of this declaration. The signature represents the identity of this declaration.
   * @return
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
   * @param signature
   */
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
  
//  public Declaration clone();
  
  /**
   * Because some declarations, such as formal generic parameters, are stubs for other declarations,
   * the lookup process invokes selectionDeclaration() on a declaration before giving it to the selection
   * method of a DeclarationSelector. A formal generic parameter, for example, will return a stub type with
   * the same name as itself, but which behaves like the upperbound of its type constraints with respect to
   * lookup, yet has its own unique identity. By creating a type with the same name as itself, the DeclarationSelector 
   * for types does not have to know about the existence of formal generic parameters. 
   * In most cases this method simply returns the current object.
   * 
   * Resolve this declaration. In case of a direct declaration, the
   * method returns the current object. In case of an indirection, the
   * indirection will be resolved, and the resulting element will be
   * returned.
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
   * As explained in the selectionDeclaration method, formal generic parameters create stub types for
   * matching. In case of an instantiated generic parameter, though, the end result of a lookup
   * should be the actual type argument, and not a stub. Therefore, the resolveForResult method
   * performs a final transformation. In case of a stub of an instantiated generic parameter, the
   * actual type that is used as an argument is returned.
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
   * Return the declaration that declared this declaration. In most cases the declaration is the same
   * as the declarator. But for example, for variables in Java, a single variable declarator can declare
   * multiple variables. Becase the model must represent the lexical program, Java variables and variable declarators
   * are separate objects.
   * @return
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
   * Return the scope of this declaration. The scope of a declaration denotes the regions of the program
   * in which the declaration is visible.
   * 
   * @throws ModelException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Scope scope() throws ModelException;
 
  /**
   * Check whether this declaration is complete (whether all necessary elements are present). A complete declaration <b>can</b> be non-abstract or abstract. An incomplete
   * declaration, however, must always be abstract. Defined (and thus its inverse: abstract) is a dynamic property that uses
   * this method to determine whether or not it applies to this declaration.
   * @throws LookupException 
   */
  public boolean complete() throws LookupException;

  /**
   * Return the target context of this target.
   *
   * A target context is the context used to look up elements that are expressed
   * relative to a target. For example, when looking up <code>a.b</code>, 
   * first <code>a</code> is looked up in the current context. After that, 
   * <code>b</code> must be looked up in the context of the element returned by the 
   * lookup of <code>a</code>. But <code>b</code> must <b>not</b> be lookup up as 
   * if it were used in the lexical context of the class representing the type of 
   * <code>a</code>. Therefore, two contexts are provided: a lexical context and 
   * a target context.
   *
   * A few examples in the language Java:
   *   1) in "expr.f", "f" must be looked up in the static type of "expr",
   *      and not in its lexical context, which is the current lexical context.
   *   2) in "typename.f", "f" must be looked up in the type represented by "typename"
   *   3) in "packagename.f", "f" must be looked up in the package represented by "package"
   */
  public LookupContext targetContext() throws LookupException;
  
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
   * Return all cross references in the model that reference this declaration.
   * This can be an expensive operation on a model whose references are not 
   * yet cached.
   * 
   * The handler determines what will happend when a cross-reference throws a
   * LookupException. Handler.fail(LookupException.class) will cause the
   * search to stop immediately. {@link Handler#resume()} will
   * ignore the exception and cause the search to continue. If you provide
   * a handler for an exception that is no lookup exception, the effect
   * is the same as using {@link Handler#resume()}.
   * 
   * @param handler
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
     namespace().defaultNamespace().apply(new Action<CrossReference, E>(CrossReference.class) {

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

  @Override
  public default Declaration finalDeclaration() {
    return this;
  }

  @Override
  public default Declaration template() {
    return finalDeclaration();
  }

  @Override
  public default SelectionResult updatedTo(Declaration declaration) {
    return declaration;
  }

}
