/**
 * Created on 21-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rejuse.java.collections.Visitor;

import chameleon.exception.ModelException;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.method.Method;

/**
 * Calculates all the methods that are called by a given method
 * 
 * @author Tim Vermeiren
 *
 */
public class CalleesContentProvider implements ITreeContentProvider {

	/**
	 * Calculates all the methods that are called by a given method
	 */
	public Object[] getChildren(Object inputObject) {
		if (inputObject instanceof Method) {
			Method<?,?,?> method = (Method) inputObject;
			// get all the invocations of the given method:
			List<MethodInvocation> invocations = method.descendants(MethodInvocation.class);
			// get all the methods of these invocations:
			final List<Method> calledMethods = new ArrayList<Method>();
			new Visitor<MethodInvocation>(){
				@Override
				public void visit(MethodInvocation invocation) {
					try {
						calledMethods.add(invocation.getElement());
					} catch (ModelException e) {
						e.printStackTrace();
					}
				}
			}.applyTo(invocations);
			// return result:
			return calledMethods.toArray();
		} else if(inputObject instanceof RootMethod){
			Method method = ((RootMethod)inputObject).getMethod();
			return new Object[]{method};
		}
		return null;
	}

	public Object getParent(Object inputObject) {
		return null;
	}

	public boolean hasChildren(Object inputObject) {
		return true;
	}

	public Object[] getElements(Object inputObject) {
		return getChildren(inputObject);
	}

	public void dispose() {
		// NOP
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// NOP
	}

}
