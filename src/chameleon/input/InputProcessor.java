package chameleon.input;

import chameleon.core.element.Element;
import chameleon.tool.Processor;

/**
 * An interface for processors used while reading a model from source files.
 * 
 * @author Marko van Dooren
 */
public interface InputProcessor extends Processor {

	/**
	 * 
	 * Set the location of the given element to the given offset and length. defined by the string dectype
	 * 
	 * @param element The element to which the metadata must be added.
	 * 
	 * @param offset offset of the position
	 * @param length length of the position
	 * @param dectype
	 */
	public void setLocation(Element element, int offset, int length, String dectype);

}
