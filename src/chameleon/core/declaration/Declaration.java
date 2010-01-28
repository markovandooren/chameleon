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
 * the signature.
 * 
 * Each declaration results in the lookup of an actual declaration of type D. This type ensures that 
 * if a declaration is just a stub, such as a generic parameter, the transformation performed by the resolveForResult 
 * method returns a declaration of the same family. In case of a type parameter, this ensures that the stub type will be transformed
 * into a type.
 */
public interface Declaration<E extends Declaration<E,P,S,D>, 
                             P extends Element, 
                             S extends Signature,
                             D extends Declaration> extends Element<E,Element>{

	/**
	 * Return the signature of this declaration
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public S signature();
  
  //public void setSignature(Signature signature);
  
  public E clone();
  
  /**
   * Return an alias 
   * @return
   */
//  public Declaration alias(S sig);
  
  /**
   * Because some declarations, such as formal generic parameters, are stubs for other declarations,
   * the lookup process invokes resolveForMatch on a declaration before giving it to the selection
   * method of a DeclarationSelector. A formal generic parameter, for example, will return a stub type with
   * the same name as itself, but which behaves like the upperbound of its type constraints. By creating
   * a type with the same name as itself, the DeclarationSelector for types does not have to know
   * about the existance of formal generic parameters. In most cases this method simply returns the current object.
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
  public Declaration<?,?,?,D> selectionDeclaration() throws LookupException;

  /**
   * As explained in the resolveForMatch method, formal generic parameters create stub types for
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
