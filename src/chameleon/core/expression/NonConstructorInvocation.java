package chameleon.core.expression;


import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.method.Method;
import chameleon.exception.ModelException;
import chameleon.oo.type.Type;

/**
 * This class represents invocations that do not invoke a constructor, and thus have a name.
 * 
 * @author Marko van Dooren
 */
public abstract class NonConstructorInvocation<E extends NonConstructorInvocation<E,D>, D extends Method> extends Invocation<E,D> {

  /**
   * @param target
   */
  public NonConstructorInvocation(InvocationTarget target) {
    super(target);
  }


  public Type actualType() throws LookupException {
	    try {
				Method method = getMethod();
				if (method != null) {
				  return method.returnType();
				}
				else {
				  getMethod();
				  throw new LookupException("Could not find method of constructor invocation", this);
				}
			} catch (LookupException e) {
//				e.printStackTrace();
//				getMethod();
				throw e;
			}
	  }


//  /**
//   * @param inv
//   * @param map
//   * @return
//   */
//  public boolean superOf(InvocationTarget inv) throws LookupException {
//  	throw new Error("Implement exception anchors again");
//    if(! (inv instanceof NonConstructorInvocation)) {
//      return false;
//    }
//    NonConstructorInvocation nc = (NonConstructorInvocation)inv;
//    Method method = nc.getMethod();
//    boolean result = method.canImplement(getMethod()) &&
//                    (
//                      ((getTarget() == null) && (nc.getTarget() == null)) ||
//                      (((getTarget() != null) && (nc.getTarget() != null)) && (getTarget().compatibleWith(nc.getTarget())))
//                    );
//    List params = getActualParameters();
//    List otherParams = nc.getActualParameters();
//    for(int i=0; i< params.size(); i++) {
//      result = result && ((InvocationTarget)params.get(i)).compatibleWith((InvocationTarget)otherParams.get(i));
//    }
//    return result;
//  }
  
//  public void prefix(InvocationTarget target) throws LookupException {
//    if(getTarget() == null) {
//      setTarget(target);
//    }
//    else {
//      getTarget().prefixRecursive(target);
//    }
//  }
  
}
