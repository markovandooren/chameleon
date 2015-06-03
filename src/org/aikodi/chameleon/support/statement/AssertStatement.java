package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.util.association.Single;

public class AssertStatement extends ExpressionContainingStatement {

  /**
   * @param expression
   */
  public AssertStatement(Expression expression) {
    super(expression);
  }

  @Override
protected AssertStatement cloneSelf() {
    return new AssertStatement(null);
  }

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _messageExpression = new Single<Expression>(this);

  
  public Expression messageExpression() {
    return _messageExpression.getOtherEnd();
  }
  
  public void setMessageExpression(Expression expression) {
    set(_messageExpression,expression);
  }

  @Override
  public Verification verifySelf() {
  	Verification result = super.verifySelf();
  	Expression expr = getExpression();
  	try {
  	  if(expr != null && ! expr.getType().subtypeOf(language(ObjectOrientedLanguage.class).booleanType(view().namespace()))) {
  		  result = result.and(new BasicProblem(this, "The condition is not a boolean."));
  	  }
  	} catch(LookupException exc) {
		  result = result.and(new BasicProblem(this, "The condition is not a boolean."));
  	}
  	return result;
  }

}
