package chameleon.core.validation;

import chameleon.core.element.Element;
import chameleon.core.rule.Rule;

public abstract class ValidityRule<E extends Element> extends Rule<ValidityRule, E> {

	public ValidityRule(Class<E> elementType) {
		super(elementType);
	}

	public abstract VerificationResult verify(E element);

}
