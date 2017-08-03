package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public class WithinPointcutExpression extends DeclarationPointcutExpression<Element>{

	public WithinPointcutExpression(DeclarationPattern pattern, Class<? extends Declaration> type) {
		super(pattern);
		_type = type;
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}

	@Override
	protected WithinPointcutExpression cloneSelf() {
		return new WithinPointcutExpression(null, type());
	}

	@Override
	protected Declaration declaration(Element joinpoint) throws LookupException {
		return joinpoint.lexical().nearestAncestor(type());
	}
	
	public Class<? extends Declaration> type() {
		return _type;
	}

	private Class<? extends Declaration> _type;
}
