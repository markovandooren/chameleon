package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

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

  public List<Element> children() {
    List<Element> result = Util.createNonNullList(condition());
    Util.addNonNull(getStatement(), result);
    return result;
  }
}
