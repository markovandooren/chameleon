package org.aikodi.chameleon.support.statement;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.support.variable.LocalVariable;
import org.aikodi.chameleon.support.variable.LocalVariableDeclarator;
import org.aikodi.chameleon.util.association.Single;

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
	private Single<Expression> _expression = new Single<Expression>(this, "expression");

  
  public Expression collection() {
    return _expression.getOtherEnd();
  }
  
  public void setCollection(Expression expression) {
    set(_expression,expression);
  }

	public Element variableScopeElement() {
		return lexical().nearestAncestor(Element.class);
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
