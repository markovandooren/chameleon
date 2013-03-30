package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

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
public interface Declaration extends Element {

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
   * Return the name of this declaration.
   */
 /*@
   @ public behavior
   @
   @ post \result == signature().name();
   @*/
  public String name();
  
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
  public void setName(String name);
  
  public Declaration clone();
  
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
  public Declaration selectionDeclaration() throws LookupException;

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
  public Declaration actualDeclaration() throws LookupException;
  
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
   * For example:
   *   1) in "expr.f", "f" must be looked up in the static type of "expr",
   *      and not in its lexical context, which is the current lexical context.
   *   2) in "typename.f", "f" must be looked up in the type represented by "typename"
   *   3) in "packagename.f", "f" must be looked up in the package represented by "package"
   */
  public LookupContext targetContext() throws LookupException;
}
