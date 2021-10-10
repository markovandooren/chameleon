package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.Target;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.oo.type.Type;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class of elements representing expressions in a language. 
 * Each expression has a type.
 *  
 * @author Marko van Dooren
 */

public abstract class Expression extends ElementImpl implements CrossReferenceTarget {

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
  
	/**
   * Return the type of this expression. The actual computation of the type is
   * done in {@link #actualType()}. This method performs the caching. Enable or disable
   * caching of expression types using Config.CACHE_EXPRESSION_TYPES.
   */
 /*@
   @ public behavior
   @
   @ post \result == actualType();
   @*/
	public final Type getType() throws LookupException {
		Type result = null;
		SoftReference<Type> typeCache = _cache.get();
		result = (typeCache == null ? null : typeCache.get());
		if (result == null) {
			result = actualType();
			_cache.compareAndSet(null, new SoftReference<Type>(result));
		}
		return result;
	}

	@Override
	public void flushLocalCache() {
		super.flushLocalCache();
		boolean success = false;
		do {
			success = _cache.compareAndSet(_cache.get(), null);
		} while(! success);
	}
	
	/**
	 * Compute the type of this expression
	 * 
	 * @return the type of this expression
	 * 
	 * @throws LookupException the type could not be computed because of an
	 * exception encountered during the lookup of a declaration.
	 */
	protected abstract Type actualType() throws LookupException;

	/**
	 * @see {@link Target#targetContext()}
	 */
	@Override
	public LocalLookupContext<?> targetContext() throws LookupException {
	  return getType().targetContext();
	}
	
}
