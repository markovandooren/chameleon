package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public class WhileStatement extends IterationStatementWithExpression {

  public WhileStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  public WhileStatement clone() {
    return new WhileStatement(condition().clone(), getStatement().clone());
  }
}
