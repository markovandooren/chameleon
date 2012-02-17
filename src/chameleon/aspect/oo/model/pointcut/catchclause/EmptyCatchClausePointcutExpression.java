package chameleon.aspect.oo.model.pointcut.catchclause;

import java.util.Collections;
import java.util.List;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.Statement;
import chameleon.support.statement.EmptyStatement;

public class EmptyCatchClausePointcutExpression extends CatchClausePointcutExpression {

	@Override
	public List<? extends Element> children() {
		return Collections.emptyList();
	}

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
	public EmptyCatchClausePointcutExpression clone() {
		return new EmptyCatchClausePointcutExpression();
	}
}