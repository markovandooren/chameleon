package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public class ExactDeclarationPointcutExpression extends DeclarationPointcutExpression<Declaration> {

	public ExactDeclarationPointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends Declaration> joinPointType() throws LookupException {
		return Declaration.class;
	}

	@Override
	public ExactDeclarationPointcutExpression cloneSelf() {
		return new ExactDeclarationPointcutExpression(null);
	}

	@Override
	protected Declaration declaration(Declaration joinpoint) throws LookupException {
		return joinpoint;
	}

}
