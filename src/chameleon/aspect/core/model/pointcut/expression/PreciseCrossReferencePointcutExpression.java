package chameleon.aspect.core.model.pointcut.expression;

import chameleon.aspect.core.model.pointcut.pattern.CrossReferencePattern;
import chameleon.core.reference.CrossReference;
import chameleon.util.association.Single;

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
