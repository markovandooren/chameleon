package chameleon.core.declaration;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.scope.Scope;

public interface Declaration<E extends Declaration<E,P,S,F>, 
                             P extends DeclarationContainer, 
                             S extends Signature,
                             F extends Declaration> extends Element<E,P>{

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
  
  public E clone();
  
  /**
   * Return an alias 
   * @return
   */
//  public Declaration alias(S sig);
  
  /**
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
  public Declaration resolveForMatch() throws LookupException;
  
  public F resolveForResult() throws LookupException;
  
  /**
   * Return the scope of this declaration.
   * @return
   * @throws MetamodelException
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Scope scope() throws MetamodelException; 
 
}
