package chameleon.support.expression;

import org.rejuse.association.SingleAssociation;

import chameleon.oo.expression.Expression;

public abstract class ExpressionContainingExpression extends Expression {

	public ExpressionContainingExpression(Expression expr) {
		setExpression(expr);
	}
	
	/**
	 * FIRST
	 */
  
	private SingleAssociation<ExpressionContainingExpression,Expression> _first = new SingleAssociation<ExpressionContainingExpression,Expression>(this);

  /**
   * Return the first expression
   */
  public Expression getExpression() {
    return _first.getOtherEnd();
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
    setAsParent(_first,expression);
  }


}
