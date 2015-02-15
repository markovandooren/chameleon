package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.java.collections.RobustVisitor;

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

  @Override
public StatementExprList cloneSelf() {
    return new StatementExprList();
  }

  @Override
public CheckedExceptionList getCEL() throws LookupException {
    final CheckedExceptionList cel = new CheckedExceptionList();
    try {
      new RobustVisitor() {
        @Override
      public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getCEL());
          return null;
        }

        @Override
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

  @Override
public CheckedExceptionList getAbsCEL() throws LookupException {
    final CheckedExceptionList cel = new CheckedExceptionList();
    try {
      new RobustVisitor() {
        @Override
      public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getAbsCEL());
          return null;
        }

        @Override
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

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
   public List<? extends Declaration> declarations() throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public LookupContext localContext() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}
	
	@Override
   public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
