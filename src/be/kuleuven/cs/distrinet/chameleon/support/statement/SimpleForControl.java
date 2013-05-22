package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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

  protected SimpleForControl cloneSelf() {
    return new SimpleForControl(null, null, null);
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
	public Verification verifySelf() {
		return Valid.create();
	}

}
