package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.event.name.NameChanged;

/**
 * <p>
 * A cross-reference that contains the name of a declaration. Examples are
 * references to types and variables, or method calls.
 * </p>
 * 
 * 
 * <h3>Events</h3>
 * 
 * <p>
 * When the {@link #name()} of a cross-reference with a name changes, it sends a
 * {@link NameChanged} event.
 * </p>
 * 
 * @author Marko van Dooren
 *
 * @param <D>
 *          The type of the referenced declaration.
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
