package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.aspect.Aspect;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 *
 *	A Pointcut picks out joinpoints in the program flow.
 *
 *	TODO: more doc
 * 	
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public abstract class Pointcut extends ElementImpl implements Declaration {
	
	public Pointcut() {
		
	}
	
	public Pointcut(PointcutExpression expression) {
		setExpression(expression);
	}
	
	/**
	 * 	Get the Aspect that this Pointcut belongs to
	 */
	public Aspect aspect() {
		return (Aspect) parent();
		
	}
	
	private Single<PointcutExpression<?>> _expression = new Single<PointcutExpression<?>>(this);
	
	public PointcutExpression<?> expression() {
		return _expression.getOtherEnd();
	}
	
	protected void setExpression(PointcutExpression<?> expression) {
		set(_expression, expression);
	}
	
	protected abstract Pointcut cloneSelf();
	
	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if (aspect() == null) {
			result = result.and(new BasicProblem(this, "Pointcuts must be defined within aspects."));
		}
		return result;
	}
	
	@Override
	public Declaration selectionDeclaration() throws LookupException {
		return this;
	}
	
	@Override
	public Declaration actualDeclaration() throws LookupException {
		return this;
	}
	

	@Override
	public Declaration declarator() {
		return this;
	}

//	@Override
//	public abstract Signature signature();
//
//	@Override
//	public abstract void setSignature(Signature signature);
//
//	@Override
//	public abstract void setName(String name);

	@Override
	public Scope scope() throws ModelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
	}

	@Override
	public boolean complete() throws LookupException {
		return true;
	}
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}
	
	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
