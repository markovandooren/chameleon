package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;

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
