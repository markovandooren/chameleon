package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public class SynchronizedStatement extends StatementExprStatement {

  public SynchronizedStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  @Override
protected SynchronizedStatement cloneSelf() {
    return new SynchronizedStatement(null,null);
  }
}
