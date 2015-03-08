package org.aikodi.chameleon.input;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.plugin.ViewPlugin;

/**
 * A connector that allows a tool to retrieve the 'source code' of a particular element.
 * 
 * @author Marko van Dooren
 */
public interface SourceManager extends ViewPlugin {
	
	/**
	 * Return the text representing the 'source code' for the given element.
	 */
	public String text(Element element) throws NoLocationException;

}
