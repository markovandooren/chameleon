package chameleon.support.expression;

import org.rejuse.association.SingleAssociation;

import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;

public abstract class TernaryExpression<E extends TernaryExpression> extends BinaryExpression<E> {

	public TernaryExpression(Expression first, Expression second, Expression third) {
		super(first,second);
		setThird(third);
	}
	
	/**
	 * THIRD
	 */
	private SingleAssociation<TernaryExpression,Expression> _third = new SingleAssociation<TernaryExpression,Expression>(this);

  public Expression getThird() {
    return _third.getOtherEnd();
  }

  public void setThird(Expression expression) {
  	if(expression != null) {
      _third.connectTo(expression.parentLink());
  	} else {
  		_third.connectTo(null);
  	}
  }

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
    if(getThird() == null) {
    	result = result.and(new BasicProblem(this,"The expression has no third expression."));
    }
    return result;
	}

}
