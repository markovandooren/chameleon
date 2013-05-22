package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;

public class CrossReferencePointcutExpression extends DeclarationPointcutExpression<CrossReference> {

	public CrossReferencePointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends CrossReference> joinPointType() throws LookupException {
		return CrossReference.class; 
	}

	@Override
	protected CrossReferencePointcutExpression cloneSelf() {
		return new CrossReferencePointcutExpression(null);
	}

	protected Declaration declaration(CrossReference joinpoint) throws LookupException {
		return joinpoint.getElement();
	}

}
