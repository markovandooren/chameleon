package chameleon.support.statement;


import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public abstract class StatementExprStatement<E extends StatementExprStatement> extends StatementContainingStatement<E> {
  
  public StatementExprStatement(Statement<E> statement, Expression expression) {
    super(statement);
    setExpression(expression);
  }

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<StatementExprStatement,Expression> _expression = new SingleAssociation<StatementExprStatement,Expression>(this);

  
  public Expression<? extends Expression> expression() {
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

}
