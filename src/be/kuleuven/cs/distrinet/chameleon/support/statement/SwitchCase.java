package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListContainer;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.java.collections.RobustVisitor;
import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;

/**
 * @author Marko van Dooren
 */
public class SwitchCase extends ElementImpl implements StatementListContainer, ExceptionSource {

  protected SwitchCase() {
	}
  
  public SwitchCase(SwitchLabel label) {
  	setLabel(label);
  }

	/**
	 * STATEMENTS
	 */
	private Multi<Statement> _statements = new Multi<Statement>(this);


  public OrderedMultiAssociation getStatementsLink() {
    return _statements;
  }

  public void addStatement(Statement statement) {
    add(_statements,statement);
  }

  public void removeStatement(Statement statement) {
    remove(_statements,statement);
  }

  public List<Statement> statements() {
    return _statements.getOtherEnds();
  }

	/**
	 * LABELS
	 */
	private Single<SwitchLabel> _labels = new Single<SwitchLabel>(this);

  public void setLabel(SwitchLabel label) {
  	set(_labels,label);
  }

  public SwitchLabel getLabel() {
    return _labels.getOtherEnd();
  }

  /**
   * @return
   */
  protected SwitchCase cloneSelf() {
    return new SwitchCase();
  }

  public CheckedExceptionList getCEL() throws LookupException {
    final CheckedExceptionList cel = new CheckedExceptionList();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getCEL());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(statements());
      return cel;
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
    final CheckedExceptionList cel = new CheckedExceptionList();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getAbsCEL());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(statements());
      return cel;
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public int getIndexOf(Statement statement) {
    return statements().indexOf(statement) + 1;
  }

	public List<Statement> statementsAfter(Statement statement) {
		List<Statement> statements = statements(); 
		int index = statements.indexOf(statement);
		// returns a view on a clone of _statements (getStatements() clones the list).
		// the view depends on the local variable, but since no other references exist
		// this is not a problem.
		return statements.subList(index, statements.size());
	}

	@Override
	public Verification verifySelf() {
		return checkNull(getLabel(), "The label is missing", Valid.create());
	}
	
}
