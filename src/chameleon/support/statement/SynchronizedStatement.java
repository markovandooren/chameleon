package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class SynchronizedStatement extends StatementExprStatement<SynchronizedStatement> {

  public SynchronizedStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  public SynchronizedStatement clone() {
    return new SynchronizedStatement(expression().clone(), getStatement().clone());
  }

  public List<Element> children() {
    List<Element> result = Util.createNonNullList(expression());
    Util.addNonNull(getStatement(), result);
    return result;
  }
}
