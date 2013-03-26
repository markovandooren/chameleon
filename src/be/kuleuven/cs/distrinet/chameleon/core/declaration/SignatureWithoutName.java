package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;

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
