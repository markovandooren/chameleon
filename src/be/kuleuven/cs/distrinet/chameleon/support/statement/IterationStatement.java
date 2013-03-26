package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;

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
