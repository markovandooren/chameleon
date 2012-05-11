package chameleon.support.statement;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.support.variable.LocalVariable;
import chameleon.support.variable.LocalVariableDeclarator;
import chameleon.util.association.Single;

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
	public EnhancedForControl clone() {
		return new EnhancedForControl(variableDeclarator().clone(),collection().clone());
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

	public List<LocalVariable> declarations() throws LookupException {
		return variableDeclarator().variables();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = checkNull(variableDeclarator(), "An enhanced for control must declare a variable", Valid.create());
		result = checkNull(collection(), "An enhanced for control must indicate from which collection the variables must come.", result);
		return result;
	}

}
