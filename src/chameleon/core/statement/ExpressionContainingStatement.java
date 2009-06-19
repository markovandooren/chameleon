package chameleon.core.statement;


import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.expression.ExpressionContainer;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class ExpressionContainingStatement<E extends ExpressionContainingStatement> 
                extends Statement<E> implements ExpressionContainer<E,StatementContainer> {
	
  public ExpressionContainingStatement(Expression expression) {
    setExpression(expression);
  }
  
  public ExpressionContainingStatement() {
  	
  }

	/**
	 * EXPRESSION
	 */
	private Reference<ExpressionContainingStatement,Expression> _expression = new Reference<ExpressionContainingStatement,Expression>(this);

  
  public Expression getExpression() {
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

  public List<Element> children() {
    return Util.createNonNullList(getExpression());
  }


}
