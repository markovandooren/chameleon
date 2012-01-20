package chameleon.support.statement;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.java.collections.RobustVisitor;
import org.rejuse.java.collections.Visitor;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionSource;
import chameleon.oo.statement.Statement;
import chameleon.oo.statement.StatementImpl;
import chameleon.oo.statement.StatementListContainer;

/**
 * @author Marko van Dooren
 */
public class SwitchCase extends NamespaceElementImpl<SwitchCase> implements StatementListContainer<SwitchCase>, ExceptionSource<SwitchCase> {

  public SwitchCase() {
	}
  
  public SwitchCase(SwitchLabel label) {
  	setLabel(label);
  }

	/**
	 * STATEMENTS
	 */
	private OrderedMultiAssociation<SwitchCase,Statement> _statements = new OrderedMultiAssociation<SwitchCase,Statement>(this);


  public OrderedMultiAssociation getStatementsLink() {
    return _statements;
  }

  public void addStatement(Statement statement) {
    _statements.add(statement.parentLink());
  }

  public void removeStatement(Statement statement) {
    _statements.add(statement.parentLink());
  }

  public List<Statement> statements() {
    return _statements.getOtherEnds();
  }

	/**
	 * LABELS
	 */
	private SingleAssociation<SwitchCase,SwitchLabel> _labels = new SingleAssociation<SwitchCase,SwitchLabel>(this);

  public void setLabel(SwitchLabel label) {
  	if(label != null) {
    _labels.connectTo(label.parentLink());
  	} else {
  		_labels.connectTo(null);
  	}
  }

  public SwitchLabel getLabel() {
    return _labels.getOtherEnd();
  }

  /**
   * @return
   */
  public SwitchCase clone() {
    final SwitchCase result = new SwitchCase();
    new Visitor<Statement>() {
      public void visit(Statement element) {
        result.addStatement(element.clone());
      }
    }.applyTo(statements());
    result.setLabel(getLabel().clone());
    return result;
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

 /*@
   @ also public behavior
   @
   @ post \result.containsAll(getStatements());
   @ post \result.containsAll(getLabels());
   @*/
  public List<Element> children() {
    List result = statements();
    result.add(getLabel());
    return result;
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
	public VerificationResult verifySelf() {
		return checkNull(getLabel(), "The label is missing", Valid.create());
	}

}
