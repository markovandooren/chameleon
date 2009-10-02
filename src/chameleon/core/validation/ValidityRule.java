package chameleon.core.validation;

import chameleon.core.element.Element;
import chameleon.core.rule.Rule;

public abstract class ValidityRule extends Rule {

	public abstract VerificationResult verify(Element element);

}
