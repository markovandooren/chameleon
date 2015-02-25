package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public abstract class IterationStatement extends StatementContainingStatement {
  
  /**
   * @param expression
   */
  public IterationStatement(Statement statement) {
    super(statement);
  }

}
