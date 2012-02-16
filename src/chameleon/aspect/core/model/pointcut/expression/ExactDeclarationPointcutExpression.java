package chameleon.aspect.core.model.pointcut.expression;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public class ExactDeclarationPointcutExpression extends DeclarationPointcutExpression<Declaration> {

	public ExactDeclarationPointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends Declaration> joinPointType() throws LookupException {
		return Declaration.class;
	}

	@Override
	public List<? extends Element> children() {
		return new ArrayList<Element>();
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
