package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class DoStatement extends IterationStatementWithExpression {

  public DoStatement(Expression expression, Statement statement) {
    super(statement, expression);
  }

  public DoStatement clone() {
    return new DoStatement(condition().clone(), getStatement().clone());
  }

  public List<Element> children() {
    List<Element> result = Util.createNonNullList(condition());
    Util.addNonNull(getStatement(), result);
    return result;
  }

}
