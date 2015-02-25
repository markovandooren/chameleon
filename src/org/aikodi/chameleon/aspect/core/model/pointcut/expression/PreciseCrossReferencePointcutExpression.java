package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.aspect.core.model.pointcut.pattern.CrossReferencePattern;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.association.Single;

public class PreciseCrossReferencePointcutExpression extends CrossReferencePointcutExpression {

	/**
	 * This constructor automatically takes the conjunction of the given pattern and a pattern that
	 * requires the nearest ancestor of the given type to be the declaration referenced by the
	 * given cross reference.
	 */
	public PreciseCrossReferencePointcutExpression(CrossReference crossRef) {
		super(new CrossReferencePattern(crossRef));
		setCrossReference(crossRef);
	}

	private Single<CrossReference> _typeReference = new Single<CrossReference>(this);
	
	public CrossReference crossReference() {
		return _typeReference.getOtherEnd();
	}
	
	private void setCrossReference(CrossReference reference) {
		set(_typeReference, reference);
	}
	
}
