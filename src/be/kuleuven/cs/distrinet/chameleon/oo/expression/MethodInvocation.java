package be.kuleuven.cs.distrinet.chameleon.oo.expression;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceWithArguments;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceWithTarget;
import be.kuleuven.cs.distrinet.chameleon.core.reference.UnresolvableCrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.method.DeclarationWithParameters;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.ActualTypeArgument;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 * 
 * @param <D>
 *            The type of declaration invoked by this invocation.
 */

public abstract class MethodInvocation<D extends DeclarationWithParameters>
		extends TargetedExpression implements CrossReferenceWithTarget<D> {

	private Single<CrossReferenceWithArguments> _crossReference = new Single<CrossReferenceWithArguments>(this);

	public MethodInvocation(CrossReferenceTarget target) {
		set(_crossReference, new CrossReferenceWithArguments());
		setTarget(target);
	}

	public final DeclarationSelector<D> selector() throws LookupException {
		if (_selector == null) {
			_selector = createSelector();
		}
		return _selector;
	}

	public CrossReferenceWithArguments crossReference() {
		return _crossReference.getOtherEnd();
	}

	protected abstract DeclarationSelector<D> createSelector()
			throws LookupException;

	protected DeclarationSelector<D> _selector;

	public CrossReferenceTarget getTarget() {
		return crossReference().getTarget();
	}

	public void setTarget(CrossReferenceTarget target) {
		crossReference().setTarget(target);
	}

	public void addArgument(Expression parameter) {
		crossReference().addArgument(parameter);
	}

	public void addAllArguments(List<Expression> parameters) {
		crossReference().addAllArguments(parameters);
	}

	public void removeParameter(Expression parameter) {
		crossReference().removeParameter(parameter);
	}

	public List<Expression> getActualParameters() {
		return crossReference().getActualParameters();
	}

	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ post \result == getActualParameters().size;
	 * 
	 * @
	 */
	public int nbActualParameters() {
		return crossReference().nbActualParameters();
	}

	public List<Type> getActualParameterTypes() throws LookupException {
		return crossReference().getActualParameterTypes();
	}

	public D getElement() throws LookupException {
		return (D) getElement(selector());
	}

	/**
	 * Return the method invoked by this invocation.
	 */
	/*
	 * @
	 * 
	 * @ public behavior
	 * 
	 * @
	 * 
	 * @ post \result != null;
	 * 
	 * @
	 * 
	 * @ signals (NotResolvedException) (* The method could not be found *);
	 * 
	 * @
	 */
	// public abstract D getMethod() throws MetamodelException;
	public <X extends Declaration> X getElement(DeclarationSelector<X> selector)
			throws LookupException {
		return (X) crossReference().getElement(selector);
	}

	public Declaration getDeclarator() throws LookupException {
		return crossReference().getDeclarator();
	}

//	@Override
//	public void flushLocalCache() {
//		super.flushLocalCache();
//		crossReference().flushLocalCache();
//	}

	protected D getCache() {
		return (D) crossReference().getCache();
	}

	protected void setCache(D value) {
		crossReference().setCache(value);
	}

	/**
	 * Return a clone of this invocation without target or parameters.
	 */
	/*
	 * @
	 * 
	 * @ protected behavior
	 * 
	 * @
	 * 
	 * @ post \result != null;
	 * 
	 * @
	 */
	protected abstract MethodInvocation cloneInvocation(CrossReferenceTarget target);

	public MethodInvocation clone() {
		CrossReferenceTarget target = null;
		if (getTarget() != null) {
			target = getTarget().clone();
		}
		MethodInvocation result = cloneInvocation(target);
		for (Expression element : getActualParameters()) {
			result.addArgument(element.clone());
		}
		for (ActualTypeArgument arg : typeArguments()) {
			result.addArgument(arg.clone());
		}
		return result;
	}

	// public void substituteParameter(String name, Expression expr) throws
	// MetamodelException {
	// if(getTarget()!= null) {
	// getTarget().substituteParameter(name, expr);
	// }
	// }

	public CheckedExceptionList getDirectCEL() throws LookupException {
		throw new Error();
	}

	public CheckedExceptionList getDirectAbsCEL() throws LookupException {
		throw new Error();
	}

	public List<ActualTypeArgument> typeArguments() {
		return crossReference().typeArguments();
	}
	
	public boolean hasTypeArguments() {
		return crossReference().hasTypeArguments();
	}

	public void addArgument(ActualTypeArgument arg) {
		crossReference().addArgument(arg);
	}

	public void addAllTypeArguments(List<ActualTypeArgument> args) {
		crossReference().addAllTypeArguments(args);
	}

	public void removeArgument(ActualTypeArgument arg) {
		crossReference().removeArgument(arg);
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		try {
			if (getElement() == null) {
				result = result.and(new UnresolvableCrossReference(this));
			}
		} catch(LookupException e) {
			result = result.and(new UnresolvableCrossReference(this, e.getMessage()));
		} catch(ChameleonProgrammerException e) {
			result = result.and(new UnresolvableCrossReference(this, e.getMessage()));
		}
		return result;
	}
}
