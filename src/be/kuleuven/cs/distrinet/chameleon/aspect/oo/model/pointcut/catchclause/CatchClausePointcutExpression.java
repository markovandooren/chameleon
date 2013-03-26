package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.catchclause;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.support.statement.CatchClause;

public abstract class CatchClausePointcutExpression extends AbstractPointcutExpression<Statement> {

	@Override
	public MatchResult match(Statement joinpoint) throws LookupException {
		if (! doesMatch(joinpoint)) {
			return MatchResult.noMatch();
		}
		return new MatchResult<Element>(this, joinpoint);
	}
	
	protected boolean doesMatch(Statement joinpoint) throws LookupException {
		return (joinpoint.parent() instanceof CatchClause) && (((CatchClause) joinpoint.parent()).statement() == joinpoint); 
	}

	@Override
	public Class<? extends Statement> joinPointType() throws LookupException {
		return Statement.class;
	}
}
