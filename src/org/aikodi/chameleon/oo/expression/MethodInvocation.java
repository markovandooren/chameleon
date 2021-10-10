package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceWithTarget;
import org.aikodi.chameleon.core.reference.UnresolvableCrossReference;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.method.DeclarationWithParameters;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.generics.TypeArgument;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * @author Marko van Dooren
 * 
 * @param <D>import java.lang.ref.SoftReference;

 *            The type of declaration invoked by this invocation.
 */

public abstract class MethodInvocation<D extends DeclarationWithParameters>
extends TargetedExpression implements CrossReferenceWithTarget<D> {

	public MethodInvocation(CrossReferenceTarget target) {
		setTarget(target);
	}

	public final DeclarationSelector<D> selector() throws LookupException {
		if (_selector == null) {
			_selector = createSelector();
		}
		return _selector;
	}


	protected abstract DeclarationSelector<D> createSelector()
			throws LookupException;

	private DeclarationSelector<D> _selector;

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
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



	//*********************


	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(
			this);

	@Override
	public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	@Override
	public void setTarget(CrossReferenceTarget target) {
		set(_target,target);
	}

	/*********************
	 * ACTUAL PARAMETERS *
	 *********************/
	private Multi<Expression> _parameters = new Multi<Expression>(this,"arguments");
	{
		_parameters.enableCache();
	}

	public void addArgument(Expression parameter) {
		add(_parameters, parameter);
	}

	public void addAllArguments(List<? extends Expression> parameters) {
		for (Expression parameter : parameters) {
			addArgument(parameter);
		}
	}

	public void removeParameter(Expression parameter) {
		remove(_parameters,parameter);
	}

	public List<Expression> getActualParameters() {
		return _parameters.getOtherEnds();
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
		return _parameters.size();
	}

	public List<Type> getActualParameterTypes() throws LookupException {
		List<Expression> params = getActualParameters();
		final List<Type> result = Lists.create();
		for (Expression param : params) {
			Type type = param.getType();
			if (type != null) {
				result.add(type);
			} else {
				// Type ttt = ((ActualParameter)param).getType(); //DEBUG
				throw new LookupException("Cannot determine type of expression");
			}
		}
		return result;
	}

	@Override
	public D getElement() throws LookupException {
		D result = null;

		// OPTIMISATION
		result = (D) cache();
		if (result != null) {
			return result;
		}

		synchronized(this) {
			DeclarationCollector<D> collector = new DeclarationCollector<D>(selector());
			CrossReferenceTarget target = getTarget();
			if (target == null) {
				lexicalContext().lookUp(collector);
			} else {
				target.targetContext().lookUp(collector);
			}
			result = collector.result();
			setCache(result);
			return result;
		}
	}

	private SoftReference<D> _cache;

	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_cache = null;
	}

	protected D cache() {
		return (_cache == null ? null : _cache.get());
	}

	protected void setCache(D value) {
		_cache = new SoftReference<D>(value);
	}

	public List<TypeArgument> typeArguments() {
		return _genericArguments.getOtherEnds();
	}

	public boolean hasTypeArguments() {
		return _genericArguments.size() > 0;
	}


	public void addArgument(TypeArgument arg) {
		add(_genericArguments,arg);
	}

	public void addAllTypeArguments(List<TypeArgument> args) {
		for (TypeArgument argument : args) {
			addArgument(argument);
		}
	}

	public void removeArgument(TypeArgument arg) {
		remove(_genericArguments,arg);
	}

	private Multi<TypeArgument> _genericArguments = new Multi<TypeArgument>(this,"type arguments");

}
