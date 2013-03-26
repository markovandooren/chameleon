package be.kuleuven.cs.distrinet.chameleon.oo.statement;

import java.util.Iterator;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.scope.LexicalScope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

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
  
  public boolean geRecursive(Scope other)  {
  	return (
		    (other instanceof StatementListScope) && 
        ((StatementListScope)other).getStatement().ancestors().contains(getContainer()) &&
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
		boolean result = element.ancestors().contains(statement);
		Iterator<Statement> iter = getContainer().statementsAfter(getStatement()).iterator();
		while(! result && iter.hasNext()) {
			result = iter.next().ancestors().contains(statement);
		}
		return result;
	}

}
