package chameleon.core.expression;

import java.util.Set;

import org.rejuse.java.collections.RobustVisitor;

import chameleon.core.Config;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.Target;
import chameleon.core.namespace.Namespace;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.Type;

/**
 * @author Marko van Dooren
 */

public abstract class Expression<E extends Expression> extends InvocationTarget<E,Element> {

	/**
	 * Return the type of this expression.
	 */
	public final Type getType() throws LookupException {
		Type result = null;
		if(Config.CACHE_EXPRESSION_TYPES) {
			result = _typeCache;
		}
		if(result == null) {
		  result = actualType();
			if(Config.CACHE_EXPRESSION_TYPES) {
				_typeCache = result;
			}
		}
		return result;
	}
	
	private Type _typeCache;
	
	protected abstract Type actualType() throws LookupException;

	/**
	 * @see {@link Target#targetContext()}
	 */
	@SuppressWarnings("unchecked")
  public LookupStrategy targetContext() throws LookupException {
    return getType().targetContext();
  }



// /*@
//   @ public behavior
//   @
//   @ post \result == getParent().getNearestType();
//   @*/
//  public Type getNearestType() {
//    return parent().getNearestType();
//  }


  public boolean subOf(InvocationTarget target) throws LookupException {
    return false;
  }
  
  public boolean compatibleWith(InvocationTarget target) throws LookupException {
    return superOf(target) || target.subOf(this);
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

  
  /**
   * See superclass.
   */
  public final Set getExceptions() throws LookupException {
    final Set result = getDirectExceptions();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          result.addAll(((InvocationTarget)element).getExceptions());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(children());
      return result;
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

 /*@
   @ public behavior
   @
   @ post \result == getPackage().getDefaultPackage(); 
   @*/
  public Namespace getDefaultNamespace() {
    return getNamespace().defaultNamespace();
  }
  
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
