package chameleon.core.expression;

import org.rejuse.association.SingleAssociation;


/**
 * @author Marko van Dooren
 */
public abstract class BinaryExpression extends Expression<BinaryExpression> {
  
  public BinaryExpression(Expression first, Expression second) {
    setFirst(first);
    setSecond(second);
  }

	/**
	 * FIRST
	 */
  
	private SingleAssociation<BinaryExpression,Expression> _first = new SingleAssociation<BinaryExpression,Expression>(this);

  /**
   * Return the first expression
   */
  public Expression<? extends Expression> getFirst() {
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
  public void setFirst(Expression expression) {
    _first.connectTo(expression.parentLink());
  }

	/**
	 * SECOND
	 */
	private SingleAssociation<BinaryExpression,Expression> _second = new SingleAssociation<BinaryExpression,Expression>(this);

  
  /**
   * Return the second expression
   */
  public Expression<? extends Expression> getSecond() {
    return _second.getOtherEnd();
  }
  
  /**
   * Set the second expression
   */
 /*@
   @ public behavior
   @
   @ post getSecond().equals(second); 
   @*/
  public void setSecond(Expression expression) {
    _second.connectTo(expression.parentLink());
  }
  
}
