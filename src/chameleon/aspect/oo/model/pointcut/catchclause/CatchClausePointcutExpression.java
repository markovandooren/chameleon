package chameleon.aspect.oo.model.pointcut.catchclause;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Statement;
import chameleon.support.statement.CatchClause;

public abstract class CatchClausePointcutExpression<E extends CatchClausePointcutExpression<E>> extends AbstractPointcutExpression<E,Statement> {

	@Override
	public MatchResult match(Statement joinpoint) throws LookupException {
		if (!(joinpoint.parent() instanceof CatchClause))
			return MatchResult.noMatch();
		
		if (((CatchClause) joinpoint.parent()).statement() != joinpoint)
			return MatchResult.noMatch();
		
		return new MatchResult<Element>(this, joinpoint);
	}

	@Override
	public Class<? extends Statement> joinPointType() throws LookupException {
		return Statement.class;
	}
}