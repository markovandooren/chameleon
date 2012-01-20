package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.support.variable.LocalVariable;
import chameleon.support.variable.LocalVariableDeclarator;
import chameleon.util.Util;

public class EnhancedForControl extends ForControl<EnhancedForControl> {

	
	
	public EnhancedForControl(LocalVariableDeclarator variable, Expression expression) {
		setVariableDeclarator(variable);
		setCollection(expression);
	}

	public LocalVariableDeclarator variableDeclarator() {
		return _variable.getOtherEnd();
	}
	
	public void setVariableDeclarator(LocalVariableDeclarator variable) {
		if(variable != null) {
			_variable.connectTo(variable.parentLink());
		} else {
			_variable.connectTo(null);
		}
	}
	
	private SingleAssociation<EnhancedForControl,LocalVariableDeclarator> _variable = new SingleAssociation<EnhancedForControl,LocalVariableDeclarator>(this);
	
	@Override
	public EnhancedForControl clone() {
		return new EnhancedForControl(variableDeclarator().clone(),collection().clone());
	}

	public List<Element> children() {
		List<Element> result = new ArrayList<Element>();
		Util.addNonNull(collection(), result);
		Util.addNonNull(variableDeclarator(), result);
		return result;
	}

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<EnhancedForControl,Expression> _expression = new SingleAssociation<EnhancedForControl,Expression>(this);

  
  public Expression collection() {
    return _expression.getOtherEnd();
  }
  
  public void setCollection(Expression expression) {
    if(expression != null) {
    	_expression.connectTo(expression.parentLink());
    }
    else {
      _expression.connectTo(null); 
    }
  }

	public NamespaceElement variableScopeElement() {
		return nearestAncestor(NamespaceElement.class);
	}

	public List<LocalVariable> declarations() throws LookupException {
		return variableDeclarator().variables();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = checkNull(variableDeclarator(), "An enhanced for control must declare a variable", Valid.create());
		result = checkNull(collection(), "An enhanced for control must indicate from which collection the variables must come.", result);
		return result;
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

}
