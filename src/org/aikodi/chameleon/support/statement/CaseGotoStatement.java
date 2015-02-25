package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.expression.Expression;

public class CaseGotoStatement extends ExpressionContainingStatement {

	public CaseGotoStatement(Expression expr) {
		super(expr);
	}
	
	@Override
	protected CaseGotoStatement cloneSelf() {
		return new CaseGotoStatement(null);
	}
}
