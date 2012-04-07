package chameleon.support.statement;

import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.Statement;
import chameleon.oo.statement.StatementImpl;
import chameleon.util.association.Single;

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
  public VerificationResult verifySelf() {
		return checkNull(getStatement(), "Statement is missing", Valid.create());
  }
}
