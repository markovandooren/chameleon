package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;


/**
 * @author Marko van Dooren
 */
public abstract class BinaryExpression extends ExpressionContainingExpression {
  
  public BinaryExpression(Expression first, Expression second) {
    super(first);
    setSecond(second);
  }
  
  public Expression getFirst() {
  	return getExpression();
  }
  
  public void setFirst(Expression expr) {
  	setExpression(expr);
  }

	/**
	 * SECOND
	 */
	private Single<Expression> _second = new Single<Expression>(this);

  
  /**
   * Return the second expression
   */
  public Expression getSecond() {
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
    set(_second,expression);
  }
 
	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
    if(getFirst() == null) {
    	result = result.and(new BasicProblem(this,"The expression has no left-hand side."));
    }
    if(getSecond() == null) {
    	result = result.and(new BasicProblem(this,"The expression has no right-hand side."));
    }
    return result;
	}

}
