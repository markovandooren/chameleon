package org.aikodi.chameleon.core.tag;

import org.aikodi.chameleon.core.element.Element;

/**
 * An interface for metadata tags that can be attached to elements.
 * 
 * @author Marko van Dooren
 */
public interface Metadata {

	/**
	 * Return the element to which this tag is attached.
	 * @return
	 */
	public Element element();
	
	public void setElement(Element element, String name);
	
	/**
	 * Disconnect this metadata from its element.
	 */
	public void disconnect();

}
