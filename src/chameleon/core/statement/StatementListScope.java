package chameleon.core.statement;

import chameleon.core.MetamodelException;
import chameleon.core.scope.Scope;

/**
 * @author Marko van Dooren
 */
public class StatementListScope extends Scope {

  
  /**
   * @param _block
   * @param _statement
   */
  public StatementListScope(StatementListContainer container, Statement statement) {
    _container = container;
    _statement = statement;
  }
  
  public boolean geRecursive(Scope other) throws MetamodelException {
    return (other instanceof StatementListScope) && 
           ((StatementListScope)other).getStatement().ancestors().contains(getContainer()) &&
           (getStatement().before(((StatementListScope)other).getStatement()));
  }
  
  public Statement getStatement() {
    return _statement;
  }
  
  public StatementListContainer getContainer() {
    return _container;
  }

	private StatementListContainer _container;

	private Statement _statement;

}
