package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * <p>A cross-reference that contains the name of a declaration. Examples
 * are references to types and variables, or method calls.</p>
 *  
 * @author Marko van Dooren
 *
 * @param <D> The type of the referenced declaration.
 */
public interface CrossReferenceWithName<D extends Declaration> extends CrossReference<D> {

  /**
   * Change the name that is used during lookup of the referenced element.
   * @param name The new name that is used during lookup of the referenced element.
   */
	public void setName(String name);
	
	/**
	 * @return the name of the referenced element.
	 */
	public String name();

}
