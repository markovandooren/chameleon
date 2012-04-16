package chameleon.support.statement;

import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.association.Single;

public class AssertStatement extends ExpressionContainingStatement {

  /**
   * @param expression
   */
  public AssertStatement(Expression expression) {
    super(expression);
  }

  public AssertStatement clone() {
    return new AssertStatement(getExpression().clone());
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
  public VerificationResult verifySelf() {
  	VerificationResult result = super.verifySelf();
  	Expression expr = getExpression();
  	try {
  	  if(expr != null && ! expr.getType().subTypeOf(language(ObjectOrientedLanguage.class).booleanType())) {
  		  result = result.and(new BasicProblem(this, "The condition is not a boolean."));
  	  }
  	} catch(LookupException exc) {
		  result = result.and(new BasicProblem(this, "The condition is not a boolean."));
  	}
  	return result;
  }

}
