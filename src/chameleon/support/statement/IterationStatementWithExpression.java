package chameleon.support.statement;

import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.util.association.Single;

public abstract class IterationStatementWithExpression extends IterationStatement {
	
	
	
	public IterationStatementWithExpression(Statement statement, Expression condition) {
		super(statement);
		setCondition(condition);
	}

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression condition() {
    return _expression.getOtherEnd();
  }
  
  public void setCondition(Expression expression) {
    set(_expression,expression);
  }

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = checkNull(condition(), "Condition is missing", super.verifySelf());
		return result;
	}


}
