package chameleon.core.reference;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.TwoPhaseDeclarationSelector;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.expression.InvocationTarget;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.member.MoreSpecificTypesOrder;
import chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import chameleon.oo.type.Type;
import chameleon.oo.type.generics.ActualTypeArgument;
import chameleon.util.Util;

public class CrossReferenceWithArguments<E extends CrossReferenceWithArguments<E>>
		extends NamespaceElementImpl<E> {
	
	public CrossReferenceWithArguments() {
		
	}

	/**
	 * TARGET
	 */
	private SingleAssociation<CrossReferenceWithArguments, InvocationTarget> _target = new SingleAssociation<CrossReferenceWithArguments, InvocationTarget>(
			this);

	public InvocationTarget getTarget() {
		return _target.getOtherEnd();
	}

	public void setTarget(InvocationTarget target) {
		if (target != null) {
			_target.connectTo(target.parentLink());
		} else {
			_target.connectTo(null);
		}
	}

	public DeclarationSelector<Declaration> selector() throws LookupException {
		return nearestAncestor(MethodInvocation.class).selector();
	}

	/*********************
	 * ACTUAL PARAMETERS *
	 *********************/
	private OrderedMultiAssociation<CrossReferenceWithArguments, Expression> _parameters = new OrderedMultiAssociation<CrossReferenceWithArguments, Expression>(
			this);

	public void addArgument(Expression parameter) {
		setAsParent(_parameters, parameter);
	}

	public void addAllArguments(List<Expression> parameters) {
		for (Expression parameter : parameters) {
			addArgument(parameter);
		}
	}

	public void removeParameter(Expression parameter) {
		_parameters.remove(parameter.parentLink());
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
		final List<Type> result = new ArrayList<Type>();
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

	/*
	 * @
	 * 
	 * @ also public behavior
	 * 
	 * @
	 * 
	 * @ post \result.contains(actualArgumentList());
	 * 
	 * @ post getTarget() != null ==> \result.contains(getTarget());
	 * 
	 * @
	 */
	public List<Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.addAll(getActualParameters());
		result.addAll(typeArguments());
		Util.addNonNull(getTarget(), result);
		return result;
	}

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

	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

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
		X result = null;

		// OPTIMISATION
		boolean cache = selector.equals(selector());
		if (cache) {
			result = (X) getCache();
		}
		if (result != null) {
			return result;
		}

		InvocationTarget<?> target = getTarget();
		if (target == null) {
			result = lexicalLookupStrategy().lookUp(selector);
		} else {
			result = target.targetContext().lookUp(selector);
		}
		if (result != null) {
			// OPTIMISATION
			if (cache) {
				setCache((Declaration) result);
			}
			return result;
		} else {
			// repeat lookup for debugging purposes.
			// Config.setCaching(false);
			if (target == null) {
				result = lexicalLookupStrategy().lookUp(selector);
			} else {
				result = target.targetContext().lookUp(selector);
			}
			throw new LookupException("Method returned by invocation is null");
		}
	}

	public E clone() {
		InvocationTarget target = null;
		if (getTarget() != null) {
			target = getTarget().clone();
		}
		final E result = (E) new CrossReferenceWithArguments<E>();
		result.setTarget(target);
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

	public List<ActualTypeArgument> typeArguments() {
		return _genericArguments.getOtherEnds();
	}

	public boolean hasTypeArguments() {
		return _genericArguments.size() > 0;
	}

	
	public void addArgument(ActualTypeArgument arg) {
		if (arg != null) {
			_genericArguments.add(arg.parentLink());
		}
	}

	public void addAllTypeArguments(List<ActualTypeArgument> args) {
		for (ActualTypeArgument argument : args) {
			addArgument(argument);
		}
	}

	public void removeArgument(ActualTypeArgument arg) {
		if (arg != null) {
			_genericArguments.remove(arg.parentLink());
		}
	}

	private OrderedMultiAssociation<CrossReferenceWithArguments, ActualTypeArgument> _genericArguments = new OrderedMultiAssociation<CrossReferenceWithArguments, ActualTypeArgument>(
			this);

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
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
