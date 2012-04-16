package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.util.Util;

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
