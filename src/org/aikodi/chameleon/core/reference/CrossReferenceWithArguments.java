package org.aikodi.chameleon.core.reference;

import java.util.List;

import java.lang.ref.SoftReference;

import org.aikodi.chameleon.core.Config;
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
import org.aikodi.chameleon.oo.type.generics.ActualTypeArgument;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

/**
 * 
 *
 * 
 * FIXME This should become an interface because MethodInvocation cannot extend this
 *       class and Expression at the same time. 
 * 
 * @author Marko van Dooren
 */
public class CrossReferenceWithArguments extends ElementImpl {
	
	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(
			this);

	public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	public void setTarget(CrossReferenceTarget target) {
		set(_target,target);
	}

	public DeclarationSelector<Declaration> selector() throws LookupException {
		return nearestAncestor(MethodInvocation.class).selector();
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

	/*
	 * @
	 * 
	 * @ also public behavior
	 * 
	 * @
	 * 
	 * @ post
	 * \result.containsAll(getMethod().getExceptionClause().getExceptionTypes
	 * (this));
	 * 
	 * @ post
	 * (getLanguage().getUncheckedException(getPackage().getDefaultPackage()) !=
	 * null) ==>
	 * 
	 * @ result.contains(getLanguage().getUncheckedException(getPackage().
	 * getDefaultPackage());
	 * 
	 * @
	 */
	// public Set getMethodExceptions() throws LookupException {
	// Set result = getMethod().getExceptionClause().getExceptionTypes(this);
	// Type rte =
	// language(ObjectOrientedLanguage.class).getUncheckedException();
	// if (rte != null) {
	// result.add(rte);
	// }
	// return result;
	// }

	/*
	 * @
	 * 
	 * @ also public behavior
	 * 
	 * @
	 * 
	 * @ post \result.containsAll(getMethodExceptions());
	 * 
	 * @ post
	 * (getLanguage().getNullInvocationException(getPackage().getDefaultPackage
	 * ()) != null) ==>
	 * 
	 * @ result.contains(getLanguage().getNullInvocationException(getPackage().
	 * getDefaultPackage());
	 * 
	 * @
	 */
	// public Set getDirectExceptions() throws LookupException {
	// Set result = getMethodExceptions();
	// if(getTarget() != null) {
	// Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(),
	// result);
	// }
	// return result;
	// }

	// public Set getDirectExceptions() throws NotResolvedException {
	// Set result = getMethodExceptions();
	// Type npe =
	// getLanguage().getNullInvocationException(getPackage().getDefaultPackage());
	// if(npe != null) {
	// result.add(npe);
	// }
	// result.addAll(getTarget().getDirectExceptions());
	// Iterator iter = getActualParameters().iterator();
	// while(iter.hasNext()) {
	// result.addAll(((Expression)iter.next()).getDirectExceptions());
	// }
	// return result;
	// }

	public Declaration getElement() throws LookupException {
		Declaration el = getElement(selector());
		if (el == null) // debug
			getElement(selector());

		return el;
	}

//	public Declaration getDeclarator() throws LookupException {
//		return getElement(new DeclaratorSelector(selector()));
//	}
//
	private SoftReference<Declaration> _cache;

	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		_cache = null;
	}

	public Declaration getCache() {
		Declaration result = null;
		if (Config.cacheElementReferences() == true) {
			result = (_cache == null ? null : _cache.get());
		}
		return result;
	}

	public void setCache(Declaration value) {
		// if(! value.isDerived()) {
		if (Config.cacheElementReferences() == true) {
			_cache = new SoftReference<Declaration>(value);
		}
		// } else {
		// _cache = null;
		// }
	}

	/**
	 * Return the method invoked by this invocation.
	 */
	/*@
	  @ public behavior
	  @
	  @ post \result != null;
	  @
	  @ signals (LookupException) (* The method could not be found *);
	  @*/
	public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		X result = null;

		// OPTIMISATION
		boolean cache = selector.equals(selector());
		if (cache) {
			result = (X) getCache();
		}
		if (result != null) {
			return result;
		}

		synchronized(this) {
			if(result != null) {
				return result;
			}

		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
		CrossReferenceTarget target = getTarget();
		if (target == null) {
			lexicalContext().lookUp(collector);
		} else {
			target.targetContext().lookUp(collector);
		}
		result = collector.result();
//		if (result != null) {
//			// OPTIMISATION
			if (cache) {
				setCache(result);
			}
			return result;
//		} else {
//			// repeat lookup for debugging purposes.
//			// Config.setCaching(false);
//			if (target == null) {
//				result = lookupContext().lookUp(selector);
//			} else {
//				result = target.targetContext().lookUp(selector);
//			}
//			throw new LookupException("Method returned by invocation is null");
//		}
		}
	}

	@Override
   protected CrossReferenceWithArguments cloneSelf() {
		return new CrossReferenceWithArguments();
	}

	// public void substituteParameter(String name, Expression expr) throws
	// MetamodelException {
	// if(getTarget()!= null) {
	// getTarget().substituteParameter(name, expr);
	// }
	// }

	public List<ActualTypeArgument> typeArguments() {
		return _genericArguments.getOtherEnds();
	}

	public boolean hasTypeArguments() {
		return _genericArguments.size() > 0;
	}

	
	public void addArgument(ActualTypeArgument arg) {
		add(_genericArguments,arg);
	}

	public void addAllTypeArguments(List<ActualTypeArgument> args) {
		for (ActualTypeArgument argument : args) {
			addArgument(argument);
		}
	}

	public void removeArgument(ActualTypeArgument arg) {
		remove(_genericArguments,arg);
	}

	private Multi<ActualTypeArgument> _genericArguments = new Multi<ActualTypeArgument>(this,"type arguments");

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
