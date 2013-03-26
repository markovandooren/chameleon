package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class ExprTypeRefContainingExpression extends Expression {

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
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
    if(getTypeReference() == null) {
    	result = result.and(new BasicProblem(this,"The type is missing."));
    }
    if(getExpression() == null) {
    	result = result.and(new BasicProblem(this,"The cast is missing."));
    }
    return result;
	}


}
