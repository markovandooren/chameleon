package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;

/**
 * A problem class used to report that a signature has no name.
 * 
 * @author Marko van Dooren
 */
public class SignatureWithoutName extends BasicProblem {

	public SignatureWithoutName(Element element) {
		super(element, "the signature has no name");
	}

}
