package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.scope.Scope;
import chameleon.exception.ModelException;

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
 */
public interface Declaration<E extends Declaration<E,S,D>, 
                             S extends Signature,
                             D extends Declaration> extends Element<E>{

  /**
   * Return the signature of this declaration. The signature represents the identity of this declaration.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public S signature();
  
  /**
   * Change the signature of this declaration to the given declaration.
   * @param signature
   */
  public void setSignature(Signature signature);

  /**
   * Change the name of the signature of this declaration.
   * @param name
   */
  public void setName(String name);
  
  public E clone();
  
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
  public Declaration<?,?,D> selectionDeclaration() throws LookupException;

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
  public D actualDeclaration() throws LookupException;
  
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
  public Declaration declarator();
  
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
 
}
