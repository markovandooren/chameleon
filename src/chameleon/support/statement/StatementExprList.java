package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.java.collections.RobustVisitor;
import org.rejuse.java.collections.Visitor;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionSource;
import chameleon.oo.statement.Statement;

/**
 * A list of statement expressions as used in the initialization clause of a 
 * for statement. It contains a list of statement expressions.
 * 
 * @author Marko van Dooren
 */
public class StatementExprList extends NamespaceElementImpl implements ForInit, ExceptionSource {

	public StatementExprList() {
	}
	
	/**
	 * STATEMENT EXPRESSIONS
	 */
	private OrderedMultiAssociation<StatementExprList,StatementExpression> _statementExpressions = new OrderedMultiAssociation<StatementExprList,StatementExpression>(this);

  public void addStatement(StatementExpression statement) {
    add(_statementExpressions,statement);
  }

  public void removeStatement(StatementExpression statement) {
    remove(_statementExpressions,statement);
  }

  public List<StatementExpression> statements() {
    return _statementExpressions.getOtherEnds();
  }

  /**
   * @FIXME why is this method here?
   */
  public StatementExprList clone() {
    final StatementExprList result = new StatementExprList();
    new Visitor<StatementExpression>() {
      public void visit(StatementExpression element) {
        result.addStatement(element.clone());
      }
    }.applyTo(statements());
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

  public int getNbStatements() {
    return statements().size();
  }

 /*@
   @ also public behavior
   @
   @ post \result.equals(getStatements());
   @*/
  public List<StatementExpression> children() {
    return statements();
  }
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	public List<? extends Declaration> declarations() throws LookupException {
		return new ArrayList<Declaration>();
	}

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}
	
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}