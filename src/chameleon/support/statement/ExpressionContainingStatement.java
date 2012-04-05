package chameleon.support.statement;


import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.StatementImpl;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class ExpressionContainingStatement extends StatementImpl {
	
  public ExpressionContainingStatement(Expression expression) {
    setExpression(expression);
  }
  
  public ExpressionContainingStatement() {
  	
  }

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<ExpressionContainingStatement,Expression> _expression = new SingleAssociation<ExpressionContainingStatement,Expression>(this);

  
  public Expression getExpression() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    setAsParent(_expression,expression);
  }

  @Override
  /**
   * The default behavior is to report a problem if the expression is missing. Subclasses
   * are allowed to change that behavior.
   */
  public VerificationResult verifySelf() {
  	VerificationResult result = Valid.create();
  	if(getExpression() == null) {
  		result = result.and(new BasicProblem(this, "The expression is missing."));
  	}
  	return result;
  }

}
