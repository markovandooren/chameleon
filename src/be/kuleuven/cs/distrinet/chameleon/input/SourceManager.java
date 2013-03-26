package be.kuleuven.cs.distrinet.chameleon.input;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewPlugin;

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
