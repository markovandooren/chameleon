package chameleon.support.statement;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.Statement;
import chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public abstract class StatementContainingStatement<E extends StatementContainingStatement> extends StatementImpl<E> {
  
  public StatementContainingStatement(Statement<E> statement) {
    setStatement(statement);
  }

	/**
	 * STATEMENT
	 */
  
	private SingleAssociation<StatementContainingStatement,Statement> _statement = new SingleAssociation<StatementContainingStatement,Statement>(this);

  
  public void setStatement(Statement statement) {
    setAsParent(_statement,statement);
  }
  
  public void removeStatement() {
    _statement.connectTo(null);
  }
  
  public Statement<E> getStatement() {
    return _statement.getOtherEnd();
  }
  
  @Override
  public VerificationResult verifySelf() {
		return checkNull(getStatement(), "Statement is missing", Valid.create());
  }
}
