package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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

}
