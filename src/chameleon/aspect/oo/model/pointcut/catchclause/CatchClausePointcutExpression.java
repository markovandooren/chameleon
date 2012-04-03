package chameleon.aspect.oo.model.pointcut.catchclause;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Statement;
import chameleon.support.statement.CatchClause;

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