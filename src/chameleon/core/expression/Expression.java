package chameleon.core.expression;

import java.lang.ref.SoftReference;

import org.rejuse.java.collections.RobustVisitor;

import chameleon.core.Config;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Target;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.oo.type.Type;

/**
 * A class of elements representing expressions in a language. Each expression has a type.
 *  
 * @author Marko van Dooren
 */

public abstract class Expression<E extends Expression> extends NamespaceElementImpl<E> implements InvocationTarget<E> {

	/**
	 * Return the type of this expression. The actual computation of the type is done in actualType. This
	 * method performs the caching. Enable or disable caching of expression types using Config.CACHE_EXPRESSION_TYPES.
	 */
 /*@
   @ public behavior
   @
   @ post \result == actualType();
   @*/
	public synchronized final Type getType() throws LookupException {
		Type result = null;
		if(Config.cacheExpressionTypes()) {
			result = (_typeCache == null ? null : _typeCache.get());
		}
		if(result == null) {
		  result = actualType();
			if(Config.cacheExpressionTypes()) {
				_typeCache = new SoftReference<Type>(result);
			}
		}
		return result;
	}
	
	@Override
	public synchronized void flushLocalCache() {
		super.flushLocalCache();
		_typeCache = null;
	}
	
	private SoftReference<Type> _typeCache;
	
	protected abstract Type actualType() throws LookupException;

	/**
	 * @see {@link Target#targetContext()}
	 */
	@SuppressWarnings("unchecked")
  public LocalLookupStrategy targetContext() throws LookupException {
    return getType().targetContext();
  }

  public void prefix(InvocationTarget target) throws LookupException {
    // Do nothing by default.
  }
  
  public void prefixRecursive(InvocationTarget target) throws LookupException {
    // Do nothing by default.
  }
  
  public void substituteParameter(String name, Expression expr) throws LookupException {
    // Do nothing by default.
  }

  /**
   * Repeated because the Java type checker is dumb.
   */
  public abstract E clone();

  
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
