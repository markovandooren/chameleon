package chameleon.support.statement;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;

public class AssertStatement extends ExpressionContainingStatement<AssertStatement> {

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
	private SingleAssociation<AssertStatement,Expression> _messageExpression = new SingleAssociation<AssertStatement,Expression>(this);

  
  public Expression messageExpression() {
    return _messageExpression.getOtherEnd();
  }
  
  public void setMessageExpression(Expression expression) {
    if(expression != null) {
    	_messageExpression.connectTo(expression.parentLink());
    }
    else {
      _messageExpression.connectTo(null); 
    }
  }

  public List<Element> children() {
  	List<Element> result = super.children();
  	if(messageExpression() != null) {
  		result.add(messageExpression());
  	}
  	return result;
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
