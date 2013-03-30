package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.java.collections.RobustVisitor;
import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

/**
 * A list of statement expressions as used in the initialization clause of a 
 * for statement. It contains a list of statement expressions.
 * 
 * @author Marko van Dooren
 */
public class StatementExprList extends ElementImpl implements ForInit, ExceptionSource {

	public StatementExprList() {
	}
	
	/**
	 * STATEMENT EXPRESSIONS
	 */
	private Multi<StatementExpression> _statementExpressions = new Multi<StatementExpression>(this);

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

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	public List<? extends Declaration> declarations() throws LookupException {
		return new ArrayList<Declaration>();
	}

	@Override
	public LookupContext localContext() throws LookupException {
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
