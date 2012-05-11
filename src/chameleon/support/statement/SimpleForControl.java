package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.variable.Variable;
import chameleon.util.association.Single;

public class SimpleForControl extends ForControl {

	public SimpleForControl(ForInit init, Expression condition, StatementExprList update) {
		setForInit(init);
		setExpression(condition);
		setUpdate(update);
	}
	
  /**
	 * FOR INIT
	 */
	private Single<ForInit> _forInit = new Single<ForInit>(this);


  public Single<ForInit> getInitLink() {
    return _forInit;
  }

  public void setForInit(ForInit forInit) {
    set(_forInit,forInit);
  }

  public ForInit getForInit() {
    return _forInit.getOtherEnd();
  }

	/**
	 * UPDATE
	 */

  private Single<StatementExprList> _update = new Single<StatementExprList>(this);

  public void setUpdate(StatementExprList update) {
  	set(_update,update);
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
      init = (ForInit)getForInit().clone();
    }
    StatementExprList update = null;
    if(update() != null) {
      update = update().clone();
    }
    return new SimpleForControl(init, cond, update);
  }

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression condition() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
  	set(_expression,expression);
  }
  

	public List<? extends Declaration> declarations() throws LookupException {
		ForInit init = getForInit();
		if(init != null) {
		  return getForInit().declarations();
		} else {
			return new ArrayList<Variable>();
		}
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
