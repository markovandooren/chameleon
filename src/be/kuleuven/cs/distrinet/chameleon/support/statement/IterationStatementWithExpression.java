package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
	public Verification verifySelf() {
		Verification result = checkNull(condition(), "Condition is missing", super.verifySelf());
		return result;
	}


}
