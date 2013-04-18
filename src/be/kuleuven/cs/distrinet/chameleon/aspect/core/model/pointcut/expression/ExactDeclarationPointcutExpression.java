package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public class ExactDeclarationPointcutExpression extends DeclarationPointcutExpression<Declaration> {

	public ExactDeclarationPointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends Declaration> joinPointType() throws LookupException {
		return Declaration.class;
	}

	@Override
	public ExactDeclarationPointcutExpression clone() {
		return new ExactDeclarationPointcutExpression(pattern().clone());
	}

	@Override
	protected Declaration declaration(Declaration joinpoint) throws LookupException {
		return joinpoint;
	}

}
