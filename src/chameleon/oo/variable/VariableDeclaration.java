package chameleon.oo.variable;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LexicalLookupStrategy;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.Type;
import chameleon.util.association.Single;

public class VariableDeclaration extends ElementImpl implements DeclarationContainer {

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
		VariableDeclaration variableDeclaration = new VariableDeclaration(signature().clone(), clonedExpression);
//		if(clonedExpression != null) {
//			clonedExpression.parentLink().lock();
//		}
		return variableDeclaration;
	}

	public void setSignature(SimpleNameSignature signature) {
		set(_signature,signature);
	}
	  
	  /**
	   * Return the signature of this member.
	   */
	  public SimpleNameSignature signature() {
	    return _signature.getOtherEnd();
	  }
	  
	  private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this);

	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this);

  
  public Expression initialization() {
    return _expression.getOtherEnd();
  }
  
  public void setInitialization(Expression expression) {
    set(_expression,expression);
  }
  
  public Variable variable() {
  	Variable result = _cache;
  	if(result == null) {
  		Expression init = initialization();
  		Expression initClone = (init == null ? null : init.clone());
  		result = ((VariableDeclarator)parent()).createVariable(signature().clone(),initClone);
  		result.setUniParent(parent());
  		result.setOrigin(this);
  		transform(result);
  		_cache = result;
  	}
  	return result;
  }
  
  @Override
  public synchronized void flushLocalCache() {
  	_cache = null;
  }
  
  private Variable _cache;
  
  protected void transform(Variable variable) {
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

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		throw new ChameleonProgrammerException();
	}
	// BUG shouldn't this override lexical strategy?
	
  
}
