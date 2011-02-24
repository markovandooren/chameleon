package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LexicalLookupStrategy;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.util.Util;

public class VariableDeclaration<V extends Variable> extends NamespaceElementImpl<VariableDeclaration<V>> implements DeclarationContainer<VariableDeclaration<V>> {

	public VariableDeclaration(String name) {
		this(new SimpleNameSignature(name), null);
	}
	
	public VariableDeclaration(String name, Expression expr) {
		this(new SimpleNameSignature(name), expr);
	}
	
	public VariableDeclaration(SimpleNameSignature sig, Expression expr) {
		setInitialization(expr);
		setSignature(sig);
	}
	
	@Override
	public VariableDeclaration clone() {
		Expression expression = initialization();
		Expression clonedExpression = null;
		if(expression != null) {
			clonedExpression = expression.clone();
		}
		return new VariableDeclaration(signature().clone(), clonedExpression);
	}

	 public void setSignature(SimpleNameSignature signature) {
	    if(signature != null) {
	    	_signature.connectTo(signature.parentLink());
	    } else {
	    	_signature.connectTo(null);
	    }
	  }
	  
	  /**
	   * Return the signature of this member.
	   */
	  public SimpleNameSignature signature() {
	    return _signature.getOtherEnd();
	  }
	  
	  private SingleAssociation<VariableDeclaration, SimpleNameSignature> _signature = new SingleAssociation<VariableDeclaration, SimpleNameSignature>(this);


	public List<Element> children() {
		List<Element> result = new ArrayList<Element>();
		Util.addNonNull(initialization(), result);
		result.add(signature());
		return result;
	}

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<VariableDeclaration,Expression> _expression = new SingleAssociation<VariableDeclaration,Expression>(this);

  
  public Expression initialization() {
    return _expression.getOtherEnd();
  }
  
  public void setInitialization(Expression expression) {
    if(expression != null) {
    	_expression.connectTo(expression.parentLink());
    }
    else {
      _expression.connectTo(null); 
    }
  }
  
  public V variable() {
  	Expression init = initialization();
		Expression initClone = (init == null ? null : init.clone());
		V result = (V)((VariableDeclarator<?,V>)parent()).createVariable(signature().clone(),initClone);
  	result.setUniParent(parent());
  	result.setOrigin(this);
  	transform(result);
  	return result;
  }
  
  protected void transform(V variable) {
  }

	public List<? extends Declaration> declarations() throws LookupException {
		List<Variable> result = new ArrayList<Variable>();
		result.add(variable());
		return result;
	}
	
	/**
	 * Return a standard lexical context that is attached to this variable declaration,
	 * and to a target context which is also attached to this variable declaration.
	 */
	public LookupStrategy linearContext() throws LookupException {
		return new LexicalLookupStrategy(new LocalLookupStrategy<VariableDeclaration>(this),this);
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = checkNull(signature(), "The variable declaration has no signature", Valid.create());
		Expression initialization = initialization();
		if(initialization != null) {
			Type initType = null;
			try {
				initType = initialization.getType();
			} catch (LookupException e) {
				result = result.and(new BasicProblem(this, "Cannot calculate the type of the initialization expression"));
			}
			Type variableType = null;
			try {
				variableType = variable().getType();
			} catch (LookupException e) {
				result = result.and(new BasicProblem(this, "Cannot calculate the type of the declared variable."));
			}
			if(initType != null && variableType != null) {
				try {
					if(! initType.subTypeOf(variableType)) {
						result = result.and(new BasicProblem(this, "The type of the initializer ("+initType.getFullyQualifiedName()+") is not a subtype of the type of the declared variable ("+variableType.getFullyQualifiedName()+")."));
						initialization.getType().subTypeOf(variable().getType());
					}
				} catch (LookupException e) {
					result = result.and(new BasicProblem(this, "Cannot determine the relation between the type of the initializer and the type of the declared variable."));
				}
			}

		}
		return result;
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}
  
}
