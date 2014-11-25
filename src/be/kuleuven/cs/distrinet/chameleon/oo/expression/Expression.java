package be.kuleuven.cs.distrinet.chameleon.oo.expression;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.Target;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionSource;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.rejuse.java.collections.RobustVisitor;

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
  public LocalLookupContext<?> targetContext() throws LookupException {
    return getType().targetContext();
  }

  public void prefix(CrossReferenceTarget target) throws LookupException {
    // Do nothing by default.
  }
  
  public void prefixRecursive(CrossReferenceTarget target) throws LookupException {
    // Do nothing by default.
  }
  
  public void substituteParameter(String name, Expression expr) throws LookupException {
    // Do nothing by default.
  }

//  /**
//   * See superclass.
//   */
//  public final Set getExceptions() throws LookupException {
//    final Set result = getDirectExceptions();
//    try {
//      new RobustVisitor() {
//        public Object visit(Object element) throws LookupException {
//          result.addAll(((InvocationTarget)element).getExceptions());
//          return null;
//        }
//
//        public void unvisit(Object element, Object undo) {
//          //NOP
//        }
//      }.applyTo(children());
//      return result;
//    }
//    catch (LookupException e) {
//      throw e;
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      throw new Error();
//    }
//  }

  public CheckedExceptionList getCEL() throws LookupException {
    final CheckedExceptionList cel = getDirectCEL();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getCEL());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(children());
      return cel;
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
  public CheckedExceptionList getDirectCEL() throws LookupException {
    return new CheckedExceptionList();
  }
  
  public CheckedExceptionList getAbsCEL() throws LookupException {
    final CheckedExceptionList cel = getDirectAbsCEL();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          cel.absorb(((ExceptionSource)element).getAbsCEL());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(children());
      return cel;
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
  public CheckedExceptionList getDirectAbsCEL() throws LookupException {
    return getDirectCEL();
  }
  
}
