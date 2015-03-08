package org.aikodi.chameleon.aspect.core.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.aspect.Aspect;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.association.Single;

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
	
	@Override
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
	public Declaration template() {
		return finalDeclaration();
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
