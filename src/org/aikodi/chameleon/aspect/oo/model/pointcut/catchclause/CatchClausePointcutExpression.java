package org.aikodi.chameleon.aspect.oo.model.pointcut.catchclause;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.support.statement.CatchClause;

public abstract class CatchClausePointcutExpression extends AbstractPointcutExpression<Statement> {

	@Override
	public MatchResult match(Statement joinpoint) throws LookupException {
		if (! doesMatch(joinpoint)) {
			return MatchResult.noMatch();
		}
		return new MatchResult<Element>(this, joinpoint);
	}
	
	protected boolean doesMatch(Statement joinpoint) throws LookupException {
		return (joinpoint.lexical().parent() instanceof CatchClause) && (((CatchClause) joinpoint.lexical().parent()).statement() == joinpoint); 
	}

	@Override
	public Class<? extends Statement> joinPointType() throws LookupException {
		return Statement.class;
	}
}
