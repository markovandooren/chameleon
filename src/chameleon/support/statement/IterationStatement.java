package chameleon.support.statement;

import chameleon.core.element.Element;
import chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public abstract class IterationStatement<E extends IterationStatement> extends StatementContainingStatement<E> implements BreakableStatement<E, Element>{
  
  /**
   * @param expression
   */
  public IterationStatement(Statement<E> statement) {
    super(statement);
  }

}
