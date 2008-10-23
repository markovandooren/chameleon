package chameleon.core.statement;

import chameleon.core.MetamodelException;
import chameleon.core.accessibility.AccessibilityDomain;

/**
 * @author marko
 */
public class StatementListDomain extends AccessibilityDomain {

  
  /**
   * @param _block
   * @param _statement
   */
  public StatementListDomain(StatementListContainer container, Statement statement) {
    _container = container;
    _statement = statement;
  }
  
  public boolean geRecursive(AccessibilityDomain other) throws MetamodelException {
    return (other instanceof StatementListDomain) && 
           ((StatementListDomain)other).getStatement().getAllParents().contains(getContainer()) &&
           (getStatement().before(((StatementListDomain)other).getStatement()));
  }
  
  public Statement getStatement() {
    return _statement;
  }
  
  public StatementListContainer getContainer() {
    return _container;
  }

	/**
	 * 
	 * @uml.property name="_container"
	 * @uml.associationEnd 
	 * @uml.property name="_container" multiplicity="(1 1)"
	 */
	private StatementListContainer _container;

	/**
	 * 
	 * @uml.property name="_statement"
	 * @uml.associationEnd 
	 * @uml.property name="_statement" multiplicity="(1 1)"
	 */
	private Statement _statement;

}
