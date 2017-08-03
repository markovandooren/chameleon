package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.association.Single;

public abstract class IterationStatementWithExpression extends IterationStatement {
	
	
	
	public IterationStatementWithExpression(Statement statement, Expression condition) {
		super(statement);
		setCondition(condition);
	}

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this, "expression");

  
  public Expression condition() {
    return _expression.getOtherEnd();
  }
  
  public void setCondition(Expression expression) {
    set(_expression,expression);
  }

	@Override
	public Verification verifySelf() {
		Verification result = checkNull(condition(), "Condition is missing", super.verifySelf());
		return result;
	}


}
