package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.expression.Expression;

/**
 * @author Marko van Dooren
 */
public class StatementExpression extends ExpressionContainingStatement {

  public StatementExpression(Expression expression) {
    super(expression);
  }

  @Override
protected StatementExpression cloneSelf() {
    return new StatementExpression(null);
  }
}
