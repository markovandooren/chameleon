package org.aikodi.chameleon.support.statement;


import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.StatementImpl;
import org.aikodi.chameleon.util.association.Single;

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
	private Single<Expression> _expression = new Single<Expression>(this, "expression");

  
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
