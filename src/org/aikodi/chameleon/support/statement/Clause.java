package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public abstract class Clause extends ElementImpl implements ExceptionSource {

  public Clause(Statement statement) {
    setStatement(statement);
  }

  /**
   * Statement
   */
  private Single<Statement> _statement = new Single<Statement>(this, true, "statement");

  public void setStatement(Statement statement) {
    set(_statement, statement);
  }

  public void removeStatement() {
    _statement.connectTo(null);
  }

  public Statement statement() {
    return _statement.getOtherEnd();
  }

}
