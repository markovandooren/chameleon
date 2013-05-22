package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;

public class CaseGotoStatement extends ExpressionContainingStatement {

	public CaseGotoStatement(Expression expr) {
		super(expr);
	}
	
	@Override
	protected CaseGotoStatement cloneSelf() {
		return new CaseGotoStatement(null);
	}
}
