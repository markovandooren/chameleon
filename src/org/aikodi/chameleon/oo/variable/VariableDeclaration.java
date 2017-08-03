package org.aikodi.chameleon.oo.variable;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

import com.google.common.collect.ImmutableList;

public class VariableDeclaration extends ElementImpl implements DeclarationContainer {

	public VariableDeclaration(String name) {
		this(name, null);
	}
	
	public VariableDeclaration(String name, Expression expr) {
		setInitialization(expr);
		setName(name);
	}
	
	@Override
	protected VariableDeclaration cloneSelf() {
		return new VariableDeclaration(name(), null);
	}

  private String _name;
  
  public String name() {
  	return _name;
  }

	public void setName(String name) {
	  String old = _name;
		_name = name;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, name));
    }
	}
	
	/**
	 * EXPRESSION
	 */
	private Single<Expression> _expression = new Single<Expression>(this, "expression");

  
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
  		result = ((VariableDeclarator)parent()).createVariable(name(),initClone);
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

	@Override
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

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public Verification verifySelf() {
		Verification result = checkNull(_name, "The variable declaration has no name", Valid.create());
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
					if(! initType.subtypeOf(variableType)) {
						result = result.and(new BasicProblem(this, "The type of the initializer ("+initType.getFullyQualifiedName()+") is not a subtype of the type of the declared variable ("+variableType.getFullyQualifiedName()+")."));
						initialization.getType().subtypeOf(variable().getType());
					}
				} catch (LookupException e) {
					result = result.and(new BasicProblem(this, "Cannot determine the relation between the type of the initializer and the type of the declared variable."));
				}
			}

		}
		return result;
	}

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public LookupContext localContext() throws LookupException {
		throw new ChameleonProgrammerException();
	}
	// BUG shouldn't this override lexical strategy?
	
	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
	  return super.lookupContext(child);
	}
  
}
