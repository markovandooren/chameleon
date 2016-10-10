package org.aikodi.chameleon.core.declaration;

import java.util.List;

/**
 * A declarator is an element that declares declarations.
 * 
 * @author Marko van Dooren
 */
public interface Declarator {

	/**
	 * Return the declarations declared by this declarator.
	 * 
	 * @return The list of declarations declared by this declarator.
	 *         The list is not null and does not contain null.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post ! \result.contains(null); 
  */
	public List<Declaration> declaredDeclarations();
}
