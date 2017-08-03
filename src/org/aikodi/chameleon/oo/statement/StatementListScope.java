package org.aikodi.chameleon.oo.statement;

import java.util.Iterator;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.scope.LexicalScope;
import org.aikodi.chameleon.core.scope.Scope;

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
  
  @Override
public boolean geRecursive(Scope other)  {
  	return (
		    (other instanceof StatementListScope) && 
        ((StatementListScope)other).getStatement().lexical().ancestors().contains(getContainer()) &&
        (getStatement().before(((StatementListScope)other).getStatement()))
       ) || 
       ((other instanceof LexicalScope) && contains(((LexicalScope)other).element()));
  }
  
  public Statement getStatement() {
    return _statement;
  }
  
  public StatementListContainer getContainer() {
    return _container;
  }

	private StatementListContainer _container;

	private Statement _statement;

	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(Element element) {
		Statement statement = getStatement();
		boolean result = element.lexical().ancestors().contains(statement);
		Iterator<Statement> iter = getContainer().statementsAfter(getStatement()).iterator();
		while(! result && iter.hasNext()) {
			result = iter.next().lexical().ancestors().contains(statement);
		}
		return result;
	}

}
