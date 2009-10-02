package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;

public class SignatureWithoutName extends BasicProblem {

	public SignatureWithoutName(Element element) {
		super(element, "the signature has no name");
	}

}
