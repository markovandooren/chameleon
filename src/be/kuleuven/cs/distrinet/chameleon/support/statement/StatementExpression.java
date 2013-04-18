package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;

/**
 * @author Marko van Dooren
 */
public class StatementExpression extends ExpressionContainingStatement {

  public StatementExpression(Expression expression) {
    super(expression);
  }

  public StatementExpression clone() {
    return new StatementExpression(getExpression().clone());
  }
}
