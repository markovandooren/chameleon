package chameleon.aspect.core.model.pointcut.expression;

import chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

public class CrossReferencePointcutExpression extends DeclarationPointcutExpression<CrossReference> {

	public CrossReferencePointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends CrossReference> joinPointType() throws LookupException {
		return CrossReference.class; 
	}

	@Override
	public CrossReferencePointcutExpression clone() {
		return new CrossReferencePointcutExpression(pattern().clone());
	}

	protected Declaration declaration(CrossReference joinpoint) throws LookupException {
		return joinpoint.getElement();
	}

}
