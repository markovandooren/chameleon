package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;

public abstract class DeclarationPointcutExpression<J extends Element> extends ElementImpl implements PointcutExpression<J> {
	
	public DeclarationPointcutExpression(DeclarationPattern pattern) {
		this._pattern = pattern;
	}

//	@Override
//	public abstract DeclarationPointcutExpression<J> clone();

	private DeclarationPattern _pattern;
	
	public DeclarationPattern pattern() {
		return _pattern;
	}
	
	@Override
	public MatchResult match(J joinpoint) throws LookupException {
		if(pattern().eval(declaration(joinpoint))) {
			return new MatchResult<Element>(this, joinpoint);
		} else {
			return MatchResult.noMatch();
		}
	}

	protected abstract Declaration declaration(J joinpoint) throws LookupException;

}
