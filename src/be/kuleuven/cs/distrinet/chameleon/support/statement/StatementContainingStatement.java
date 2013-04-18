package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public abstract class StatementContainingStatement extends StatementImpl {
  
  public StatementContainingStatement(Statement statement) {
    setStatement(statement);
  }

	/**
	 * STATEMENT
	 */
  
	private Single<Statement> _statement = new Single<Statement>(this);

  
  public void setStatement(Statement statement) {
    set(_statement,statement);
  }
  
  public void removeStatement() {
    _statement.connectTo(null);
  }
  
  public Statement getStatement() {
    return _statement.getOtherEnd();
  }
  
  @Override
  public Verification verifySelf() {
		return checkNull(getStatement(), "Statement is missing", Valid.create());
  }
}
