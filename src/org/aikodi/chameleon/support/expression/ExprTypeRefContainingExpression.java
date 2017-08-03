package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

public abstract class ExprTypeRefContainingExpression extends Expression {

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

	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
    if(getTypeReference() == null) {
    	result = result.and(new BasicProblem(this,"The type is missing."));
    }
    if(getExpression() == null) {
    	result = result.and(new BasicProblem(this,"The cast is missing."));
    }
    return result;
	}


}
