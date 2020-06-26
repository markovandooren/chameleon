package org.aikodi.chameleon.core.reference;

import java.util.List;

import java.lang.ref.SoftReference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.expression.MethodInvocation;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.generics.TypeArgument;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

/**
 * 
 *
 * 
 * FIXME This should become an interface because MethodInvocation cannot extend
 * this class and Expression at the same time.
 * 
 * @author Marko van Dooren
 */
public class CrossReferenceWithArguments extends ElementImpl {

	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

	public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	public void setTarget(CrossReferenceTarget target) {
		set(_target, target);
	}

	public DeclarationSelector<Declaration> selector() throws LookupException {
		return lexical().nearestAncestor(MethodInvocation.class).selector();
	}

	/********************
	 * ACTUAL ARGUMENTS *
	 ********************/
	private Multi<Expression> _parameters = new Multi<Expression>(this, "arguments");
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
		remove(_parameters, parameter);
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
				throw new LookupException("Cannot determine type of expression");
			}
		}
		return result;
	}

	public Declaration getElement() throws LookupException {
		return getElement(selector());
	}

	private SoftReference<Declaration> _cache;

	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_cache = null;
	}

	protected Declaration cache() {
		return (_cache == null ? null : _cache.get());
	}

	protected void setCache(Declaration value) {
		_cache = new SoftReference<Declaration>(value);
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
	 * @ signals (LookupException) (* The method could not be found *);
	 * 
	 * @
	 */
	public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		X result = null;

		// OPTIMISATION
		boolean cache = selector.equals(selector());
		if (cache) {
			result = (X) cache();
		}
		if (result != null) {
			return result;
		}

		synchronized (this) {
			DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
			CrossReferenceTarget target = getTarget();
			if (target == null) {
				lexicalContext().lookUp(collector);
			} else {
				target.targetContext().lookUp(collector);
			}
			result = collector.result();
			if (cache) {
				setCache(result);
			}
			return result;
		}
	}

	@Override
	protected CrossReferenceWithArguments cloneSelf() {
		return new CrossReferenceWithArguments();
	}

	public List<TypeArgument> typeArguments() {
		return _genericArguments.getOtherEnds();
	}

	public boolean hasTypeArguments() {
		return _genericArguments.size() > 0;
	}

	public void addArgument(TypeArgument arg) {
		add(_genericArguments, arg);
	}

	public void addAllTypeArguments(List<TypeArgument> args) {
		for (TypeArgument argument : args) {
			addArgument(argument);
		}
	}

	public void removeArgument(TypeArgument arg) {
		remove(_genericArguments, arg);
	}

	private Multi<TypeArgument> _genericArguments = new Multi<TypeArgument>(this, "type arguments");

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		// try {
		// if (getElement() == null) {
		// result = result.and(new UnresolvableCrossReference(this));
		// }
		// } catch (LookupException e) {
		// result = result.and(new UnresolvableCrossReference(this));
		// }
		return result;
	}

}
