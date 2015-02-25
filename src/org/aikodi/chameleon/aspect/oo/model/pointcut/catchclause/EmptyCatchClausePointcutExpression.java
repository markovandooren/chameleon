package org.aikodi.chameleon.aspect.oo.model.pointcut.catchclause;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.support.statement.EmptyStatement;

public class EmptyCatchClausePointcutExpression extends CatchClausePointcutExpression {

	@Override
	public MatchResult match(Statement element) throws LookupException {
		if (!super.matches(element).isMatch())
			return MatchResult.noMatch();
		
		Statement joinpoint = element;
		
		if (joinpoint instanceof EmptyStatement)
			return new MatchResult(this, joinpoint);
		
		if (element instanceof Block && ((Block) joinpoint).statements().isEmpty())
			return new MatchResult(this, joinpoint);
		
		return MatchResult.noMatch();
	}

	@Override
	protected EmptyCatchClausePointcutExpression cloneSelf() {
		return new EmptyCatchClausePointcutExpression();
	}
}
