package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.validation.BasicProblem;

public class UnresolvableCrossReference extends BasicProblem {

	public UnresolvableCrossReference(CrossReference<?> element) {
		super(element, "Cross reference could not be resolved.");
	}

	public UnresolvableCrossReference(CrossReference<?> element, String msg) {
		super(element, "Cross reference could not be resolved: "+msg);
	}

}
