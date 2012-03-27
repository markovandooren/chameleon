package chameleon.aspect.core.model.pointcut;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.util.Util;

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
	
	private SingleAssociation<Pointcut, PointcutExpression<?>> _expression = new SingleAssociation<Pointcut, PointcutExpression<?>>(this);
	
	public PointcutExpression<?> expression() {
		return _expression.getOtherEnd();
	}
	
	protected void setExpression(PointcutExpression<?> expression) {
		setAsParent(_expression, expression);
	}
	
	public Pointcut clone() {
		Pointcut clone = cloneThis();
		clone.setExpression((PointcutExpression) expression().clone());
		return clone;
	}
	
	protected abstract Pointcut cloneThis();
	
	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if (aspect() == null) {
			result = result.and(new BasicProblem(this, "Pointcuts must be defined within aspects."));
		}
		return result;
	}
	
	@Override
	public List<Element> children() {
		List<Element> children = new ArrayList<Element>();
		Util.addNonNull(expression(), children);
		return children;
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
}
