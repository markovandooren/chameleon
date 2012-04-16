package chameleon.support.statement;


import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.util.association.Single;

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
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression expression() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    set(_expression,expression);
  }

}
