package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.Type;
import chameleon.oo.variable.Variable;
import chameleon.util.Util;

public class SimpleForControl extends ForControl<SimpleForControl> {

	public SimpleForControl(ForInit init, Expression condition, StatementExprList update) {
		setForInit(init);
		setExpression(condition);
		setUpdate(update);
	}
	
  /**
	 * FOR INIT
	 */
	private SingleAssociation<SimpleForControl,ForInit> _forInit = new SingleAssociation<SimpleForControl,ForInit>(this);


  public SingleAssociation<SimpleForControl,ForInit> getInitLink() {
    return _forInit;
  }

  public void setForInit(ForInit forInit) {
    if (forInit != null) {
      _forInit.connectTo(forInit.parentLink());
    }
    else {
      _forInit.connectTo(null);
    }
  }

  public ForInit getForInit() {
    return _forInit.getOtherEnd();
  }

	/**
	 * UPDATE
	 */

  private SingleAssociation<SimpleForControl,StatementExprList> _update = new SingleAssociation<SimpleForControl,StatementExprList>(this);

  public SingleAssociation<SimpleForControl,StatementExprList> getUpdateLink() {
    return _update;
  }

  public void setUpdate(StatementExprList update) {
    if (update != null) {
      _update.connectTo(update.parentLink());
    }
    else {
      _update.connectTo(null);
    }
  }

  public StatementExprList update() {
    return _update.getOtherEnd();
  }

  public SimpleForControl clone() {
    Expression cond = null;
    if(condition() != null) {
      cond = condition().clone();
    }
    ForInit init = null;
    if(getForInit() != null) {
      init = ((ForInit<? extends ForInit>)getForInit()).clone();
    }
    StatementExprList update = null;
    if(update() != null) {
      update = update().clone();
    }
    return new SimpleForControl(init, cond, update);
  }

	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		Util.addNonNull(condition(), result);
		Util.addNonNull(update(), result);
		Util.addNonNull(getForInit(), result);
		return result;
	}

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<SimpleForControl,Expression> _expression = new SingleAssociation<SimpleForControl,Expression>(this);

  
  public Expression<? extends Expression> condition() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    if(expression != null) {
      _expression.connectTo(expression.parentLink());
    }
    else {
      _expression.connectTo(null);
    }
  }
  
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}


	public List<? extends Variable> declarations() throws LookupException {
		ForInit init = getForInit();
		if(init != null) {
		  return getForInit().declarations();
		} else {
			return new ArrayList<Variable>();
		}
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
