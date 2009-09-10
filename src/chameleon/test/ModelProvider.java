package chameleon.test;

import java.io.IOException;

import chameleon.core.language.Language;
import chameleon.input.ParseException;

/**
 * An interface for providing test classes with a model to test.
 * 
 * @author Marko van Dooren
 */
public interface ModelProvider {

	/**
	 * Create a new model. The base files are processed, the predefined elements
	 * are added, and the custom files are processed.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Language model() throws ParseException, IOException;

}
