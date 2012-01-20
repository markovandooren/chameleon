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

public abstract class ExprTypeRefContainingExpression<E extends ExprTypeRefContainingExpression> 
                extends Expression<E> {

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<ExprTypeRefContainingExpression,Expression> _expression = new SingleAssociation<ExprTypeRefContainingExpression,Expression>(this);


  public Expression<? extends Expression> getExpression() {
    return _expression.getOtherEnd();
  }

  public void setExpression(Expression expression) {
  	if(expression != null) {
      _expression.connectTo(expression.parentLink());
  	} 
  	else {
  		_expression.connectTo(null);
  	}
  }

	/**
	 * TYPE
	 */
	private SingleAssociation<ExprTypeRefContainingExpression,TypeReference> _typeReference = new SingleAssociation<ExprTypeRefContainingExpression,TypeReference>(this);

  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    _typeReference.connectTo(type.parentLink());
  }

  public List<Element> children() {
  	List<Element> result = Util.createNonNullList(getExpression());
  	Util.addNonNull(getTypeReference(), result);
    return result;
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
