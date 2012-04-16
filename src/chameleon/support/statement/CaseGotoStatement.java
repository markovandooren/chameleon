package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.util.Util;

public class CaseGotoStatement extends ExpressionContainingStatement {

	public CaseGotoStatement(Expression expr) {
		super(expr);
	}
	
	@Override
	public CaseGotoStatement clone() {
		return new CaseGotoStatement(getExpression().clone());
	}
}
