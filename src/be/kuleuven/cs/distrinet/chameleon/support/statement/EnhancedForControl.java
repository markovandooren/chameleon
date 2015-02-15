package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.support.variable.LocalVariable;
import be.kuleuven.cs.distrinet.chameleon.support.variable.LocalVariableDeclarator;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class EnhancedForControl extends ForControl {

	public EnhancedForControl(LocalVariableDeclarator variable, Expression expression) {
		setVariableDeclarator(variable);
		setCollection(expression);
	}

	public LocalVariableDeclarator variableDeclarator() {
		return _variable.getOtherEnd();
	}
	
	public void setVariableDeclarator(LocalVariableDeclarator variable) {
		set(_variable,variable);
	}
	
	private Single<LocalVariableDeclarator> _variable = new Single<LocalVariableDeclarator>(this);
	
	@Override
	protected EnhancedForControl cloneSelf() {
		return new EnhancedForControl(null,null);
	}

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression collection() {
    return _expression.getOtherEnd();
  }
  
  public void setCollection(Expression expression) {
    set(_expression,expression);
  }

	public Element variableScopeElement() {
		return nearestAncestor(Element.class);
	}

	@Override
   public List<LocalVariable> declarations() throws LookupException {
		return variableDeclarator().variables();
	}

	@Override
	public Verification verifySelf() {
		Verification result = checkNull(variableDeclarator(), "An enhanced for control must declare a variable", Valid.create());
		result = checkNull(collection(), "An enhanced for control must indicate from which collection the variables must come.", result);
		return result;
	}

}
