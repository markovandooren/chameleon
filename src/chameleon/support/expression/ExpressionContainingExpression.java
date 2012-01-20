package chameleon.support.expression;

import org.rejuse.association.SingleAssociation;

import chameleon.oo.expression.Expression;

public abstract class ExpressionContainingExpression<E extends ExpressionContainingExpression> extends Expression<E> {

	public ExpressionContainingExpression(Expression expr) {
		setExpression(expr);
	}
	
	/**
	 * FIRST
	 */
  
	private SingleAssociation<E,Expression> _first = new SingleAssociation<E,Expression>((E) this);

  /**
   * Return the first expression
   */
  public Expression<? extends Expression> getExpression() {
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
    _first.connectTo(expression.parentLink());
  }


}
