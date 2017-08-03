package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.util.association.Single;

/**
 * An abstract class for expressions that contain other expressions.
 * @author Marko van Dooren
 *
 */
public abstract class ExpressionContainingExpression extends Expression {

	public ExpressionContainingExpression(Expression expr) {
		setExpression(expr);
	}
	
	/**
	 * The expression contained in this expression.
	 */
	private Single<Expression> _expression = new Single<Expression>(this, "expression");

  /**
   * Return the first expression
   */
  public Expression expression() {
    return _expression.getOtherEnd();
  }

  /**
   * Set the first expression
   */
 /*@
   @ public behavior
   @
   @ post getFirst().equals(first); 
   @*/
  public void setExpression(Expression expression) {
    set(_expression,expression);
  }


}
