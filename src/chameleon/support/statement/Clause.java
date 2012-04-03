package chameleon.support.statement;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionSource;
import chameleon.oo.statement.Statement;

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
	private SingleAssociation<Clause,Statement> _statement = new SingleAssociation<Clause,Statement>(this);

  public void setStatement(Statement statement) {
    setAsParent(_statement,statement);
  }

  public void removeStatement() {
    _statement.connectTo(null);
  }

  public Statement statement() {
    return _statement.getOtherEnd();
  }

  public CheckedExceptionList getCEL() throws LookupException {
    CheckedExceptionList result = new CheckedExceptionList();
    if(statement() != null) {
      result.absorb(statement().getCEL());
    }
    return result;
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
    CheckedExceptionList result = new CheckedExceptionList();
    if(statement() != null) {
      result.absorb(statement().getAbsCEL());
    }
    return result;
  }

}
