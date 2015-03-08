package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.util.association.Single;

public abstract class TernaryExpression extends BinaryExpression {

	public TernaryExpression(Expression first, Expression second, Expression third) {
		super(first,second);
		setThird(third);
	}
	
	/**
	 * THIRD
	 */
	private Single<Expression> _third = new Single<Expression>(this);

  public Expression getThird() {
    return _third.getOtherEnd();
  }

  public void setThird(Expression expression) {
  	set(_third,expression);
  }

	@Override
	public Verification verifySelf() {
		Verification result = super.verifySelf();
    if(getThird() == null) {
    	result = result.and(new BasicProblem(this,"The expression has no third expression."));
    }
    return result;
	}

}
