package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class DeclarationPointcutExpression<J extends Element> extends AbstractPointcutExpression<J> {
	
	public DeclarationPointcutExpression(DeclarationPattern pattern) {
		this._pattern = pattern;
	}

	@Override
	public abstract DeclarationPointcutExpression<J> clone();

	private DeclarationPattern _pattern;
	
	public DeclarationPattern pattern() {
		return _pattern;
	}
	
	@Override
	protected MatchResult match(J joinpoint) throws LookupException {
		if(pattern().eval(declaration(joinpoint))) {
			return new MatchResult<Element>(this, joinpoint);
		} else {
			return MatchResult.noMatch();
		}
	}

	protected abstract Declaration declaration(J joinpoint) throws LookupException;

}
