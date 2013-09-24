package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LexicalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextFactory;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

import com.google.common.collect.ImmutableList;

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
	protected VariableDeclaration cloneSelf() {
		return new VariableDeclaration((SimpleNameSignature)null, null);
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
  		Expression initClone = (init == null ? null : clone(init));
  		result = ((VariableDeclarator)parent()).createVariable(clone(signature()),initClone);
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
		return ImmutableList.of(variable());
	}
	
	/**
	 * Return a standard lexical context that is attached to this variable declaration,
	 * and to a target context which is also attached to this variable declaration.
	 */
	public LookupContext linearContext() throws LookupException {
		if(_linear == null) {
			LookupContextFactory lookupFactory = language().lookupFactory();
			_linear = lookupFactory.createLexicalLookupStrategy(lookupFactory.createLocalLookupStrategy(this),this);
		}
		return _linear;
	}
	
	private LookupContext _linear;

	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public Verification verifySelf() {
		Verification result = checkNull(signature(), "The variable declaration has no signature", Valid.create());
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
	public LookupContext localContext() throws LookupException {
		throw new ChameleonProgrammerException();
	}
	// BUG shouldn't this override lexical strategy?
	
  
}
