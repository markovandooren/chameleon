package chameleon.input;

import chameleon.core.element.Element;
import chameleon.plugin.LanguagePlugin;

/**
 * A connector that allows a tool to retrieve the 'source code' of a particular element.
 * 
 * @author Marko van Dooren
 */
public interface SourceManager extends LanguagePlugin {
	
	/**
	 * Return the text representing the 'source code' for the given element.
	 */
	public String text(Element element) throws NoLocationException;

}
