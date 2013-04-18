package be.kuleuven.cs.distrinet.chameleon.support.statement;


import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
  public Verification verifySelf() {
  	Verification result = Valid.create();
  	if(getExpression() == null) {
  		result = result.and(new BasicProblem(this, "The expression is missing."));
  	}
  	return result;
  }

}
