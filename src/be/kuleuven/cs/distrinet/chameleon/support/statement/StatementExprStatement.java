package be.kuleuven.cs.distrinet.chameleon.support.statement;


import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
