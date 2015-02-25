package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;

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
