package chameleon.aspect.core.model.pointcut.expression;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.pattern.CrossReferencePattern;
import chameleon.core.element.Element;
import chameleon.core.reference.CrossReference;
import chameleon.util.Util;

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

	private SingleAssociation<PreciseCrossReferencePointcutExpression, CrossReference> _typeReference = new SingleAssociation<PreciseCrossReferencePointcutExpression, CrossReference>(this);
	
	public CrossReference crossReference() {
		return _typeReference.getOtherEnd();
	}
	
	private void setCrossReference(CrossReference reference) {
		setAsParent(_typeReference, reference);
	}
	
}
