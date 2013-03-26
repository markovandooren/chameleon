package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
