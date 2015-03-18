package org.aikodi.chameleon.oo.expression;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.Target;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.oo.type.Type;

/**
 * A class of elements representing expressions in a language. Each expression has a type.
 *  
 * @author Marko van Dooren
 */

public abstract class Expression extends ElementImpl implements CrossReferenceTarget {

	/**
	 * Return the type of this expression. The actual computation of the type is done in actualType. This
	 * method performs the caching. Enable or disable caching of expression types using Config.CACHE_EXPRESSION_TYPES.
	 */
 /*@
   @ public behavior
   @
   @ post \result == actualType();
   @*/
	public final Type getType() throws LookupException {
		Type result = null;
		if(Config.cacheExpressionTypes()) {
			SoftReference<Type> tcache = _cache.get();
			result = (tcache == null ? null : tcache.get());
		}
		if(result == null) {
		  result = actualType();
			if(Config.cacheExpressionTypes()) {
				_cache.compareAndSet(null, new SoftReference<Type>(result));
			}
		}
		return result;
	}
	
	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
//		_typeCache = null;
		boolean success = false;
		do {
			success = _cache.compareAndSet(_cache.get(), null);
		} while(! success);

	}
	
	/**
	 * The type of an expression is cached to increase performance. Call {@link #flushCache()} to
	 * flush the cache of the model has changed.
	 * 
	 * The reference is stored in a soft reference to allow garbage collection of cached types.
	 * 
	 * The soft reference is stored in an atomic reference to deal with concurrent lookups of the
	 * type of this expression without needing a lock.
	 */
	private final AtomicReference<SoftReference<Type>> _cache = new AtomicReference<>();
	
	protected abstract Type actualType() throws LookupException;

	/**
	 * @see {@link Target#targetContext()}
	 */
  @Override
public LocalLookupContext<?> targetContext() throws LookupException {
    return getType().targetContext();
  }

}
