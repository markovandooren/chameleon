package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
	private Single<Statement> _statement = new Single<Statement>(this,true);

  public void setStatement(Statement statement) {
    set(_statement,statement);
  }

  public void removeStatement() {
    _statement.connectTo(null);
  }

  public Statement statement() {
    return _statement.getOtherEnd();
  }

  @Override
public CheckedExceptionList getCEL() throws LookupException {
    CheckedExceptionList result = new CheckedExceptionList();
    if(statement() != null) {
      result.absorb(statement().getCEL());
    }
    return result;
  }

  @Override
public CheckedExceptionList getAbsCEL() throws LookupException {
    CheckedExceptionList result = new CheckedExceptionList();
    if(statement() != null) {
      result.absorb(statement().getAbsCEL());
    }
    return result;
  }

}
