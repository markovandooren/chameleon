package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public class WhileStatement extends IterationStatementWithExpression {

  public WhileStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  @Override
public WhileStatement cloneSelf() {
    return new WhileStatement(null, null);
  }
}
