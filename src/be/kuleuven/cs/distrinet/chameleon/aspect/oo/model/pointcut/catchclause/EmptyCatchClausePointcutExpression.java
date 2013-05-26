package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.catchclause;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.support.statement.EmptyStatement;

public class EmptyCatchClausePointcutExpression extends CatchClausePointcutExpression {

	@Override
	public MatchResult match(Statement element) throws LookupException {
		if (!super.matches(element).isMatch())
			return MatchResult.noMatch();
		
		Statement joinpoint = (Statement) element;
		
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
