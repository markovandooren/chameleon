package be.kuleuven.cs.distrinet.chameleon.eclipse.property;

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.view.outline.ChameleonOutlineTree;

public class ChameleonPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		try {
			System.out.println(getClass());
			Object object = null;
			if(receiver instanceof Collection) {
				object = ((Collection<?>)receiver).iterator().next();	
			}
			if(object instanceof ChameleonOutlineTree) {
				ChameleonOutlineTree selection = (ChameleonOutlineTree) object;
				return testChameleonElement(selection.getElement(), property);
			} else if(object instanceof IProject){
				return testProject((IProject)object, property);
			} 
		} catch (Exception e) {
			// Return false when an exception is thrown.
		}
		return false;
	}

	/**
	 * Test whether the given element passes the test. The default
	 * return value is false.
	 * 
	 * @param element The element to be tested.
	 * @param property The property the element is tested for.
	 * @return
	 */
	protected boolean testChameleonElement(Element element, String property) {
		return false;
	}
	
	/**
	 * Test whether the given project passes the test. The default
	 * return value is false.
	 * 
	 * @param element The project to be tested.
	 * @param property The property the project is tested for.
	 * @return
	 */
	protected boolean testProject(IProject project, String property) {
		return false;
	}
}
