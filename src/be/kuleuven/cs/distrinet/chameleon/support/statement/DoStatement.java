package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public class DoStatement extends IterationStatementWithExpression {

  public DoStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  @Override
protected DoStatement cloneSelf() {
    return new DoStatement(null, null);
  }

}
