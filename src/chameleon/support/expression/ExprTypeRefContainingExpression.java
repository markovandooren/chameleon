package chameleon.support.expression;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public abstract class ExprTypeRefContainingExpression extends Expression {

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<ExprTypeRefContainingExpression,Expression> _expression = new SingleAssociation<ExprTypeRefContainingExpression,Expression>(this);


  public Expression getExpression() {
    return _expression.getOtherEnd();
  }

  public void setExpression(Expression expression) {
  	setAsParent(_expression,expression);
  }

	/**
	 * TYPE
	 */
	private SingleAssociation<ExprTypeRefContainingExpression,TypeReference> _typeReference = new SingleAssociation<ExprTypeRefContainingExpression,TypeReference>(this);

  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    setAsParent(_typeReference,type);
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
