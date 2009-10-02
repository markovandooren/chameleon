/**
 * 
 */
package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;

public class MissingSignature extends BasicProblem {

	public MissingSignature(Element element) {
		super(element, "The element has no signature.");
	}
	
}