package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.util.Util;

public class CaseGotoStatement extends ExpressionContainingStatement<CaseGotoStatement> {

	public CaseGotoStatement(Expression expr) {
		super(expr);
	}
	
	@Override
	public CaseGotoStatement clone() {
		return new CaseGotoStatement(((Expression<? extends Expression>)getExpression()).clone());
	}

	@Override
	public List<Element> children() {
		return Util.createNonNullList(getExpression());
	}

}
