package chameleon.core.declaration;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

public interface Declaration<E extends Declaration<E,P,S>, 
                             P extends DeclarationContainer, 
                             S extends Signature> extends Element<E,P>{

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
  public Declaration resolve() throws MetamodelException;

}
