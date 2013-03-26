package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public class CaseGotoStatement extends ExpressionContainingStatement {

	public CaseGotoStatement(Expression expr) {
		super(expr);
	}
	
	@Override
	public CaseGotoStatement clone() {
		return new CaseGotoStatement(getExpression().clone());
	}
}
