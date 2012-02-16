package chameleon.support.statement;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;

public abstract class IterationStatementWithExpression extends IterationStatement {
	
	
	
	public IterationStatementWithExpression(Statement statement, Expression condition) {
		super(statement);
		setCondition(condition);
	}

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<IterationStatementWithExpression,Expression> _expression = new SingleAssociation<IterationStatementWithExpression,Expression>(this);

  
  public Expression condition() {
    return _expression.getOtherEnd();
  }
  
  public void setCondition(Expression expression) {
    setAsParent(_expression,expression);
  }

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = checkNull(condition(), "Condition is missing", super.verifySelf());
		return result;
	}


}
