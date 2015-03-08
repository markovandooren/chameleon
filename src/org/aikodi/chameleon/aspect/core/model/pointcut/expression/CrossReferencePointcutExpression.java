package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

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

	@Override
   protected Declaration declaration(CrossReference joinpoint) throws LookupException {
		return joinpoint.getElement();
	}

}
