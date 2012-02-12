/**
 * Created on 14-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import chameleon.oo.method.Method;

/**
 * Used to encapsulate the root method of the call hierarchy.
 * Otherwise the root method is not visible.
 * 
 * 
 * @author Tim Vermeiren
 */
public class RootMethod {
	
	private Method method;

	/**
	 * @param method
	 */
	public RootMethod(Method method) {
		this.method = method;
	}
	
	public Method getMethod(){
		return method;
	}
	
}
