package chameleon.support.statement;


import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.StatementImpl;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public abstract class ExpressionContainingStatement extends StatementImpl {
	
  public ExpressionContainingStatement(Expression expression) {
    setExpression(expression);
  }
  
  public ExpressionContainingStatement() {
  	
  }

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression getExpression() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    set(_expression,expression);
  }

  @Override
  /**
   * The default behavior is to report a problem if the expression is missing. Subclasses
   * are allowed to change that behavior.
   */
  public VerificationResult verifySelf() {
  	VerificationResult result = Valid.create();
  	if(getExpression() == null) {
  		result = result.and(new BasicProblem(this, "The expression is missing."));
  	}
  	return result;
  }

}
