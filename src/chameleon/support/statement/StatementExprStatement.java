package chameleon.support.statement;


import org.rejuse.association.SingleAssociation;

import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public abstract class StatementExprStatement extends StatementContainingStatement {
  
  public StatementExprStatement(Statement statement, Expression expression) {
    super(statement);
    setExpression(expression);
  }

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<StatementExprStatement,Expression> _expression = new SingleAssociation<StatementExprStatement,Expression>(this);

  
  public Expression expression() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    setAsParent(_expression,expression);
  }

}
