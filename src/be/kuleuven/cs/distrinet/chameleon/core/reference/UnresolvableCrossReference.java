package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;

public class UnresolvableCrossReference extends BasicProblem {

	public UnresolvableCrossReference(CrossReference element) {
		super(element, "Cross reference could not be resolved.");
	}

	public UnresolvableCrossReference(CrossReference element, String msg) {
		super(element, "Cross reference could not be resolved: "+msg);
	}

}
