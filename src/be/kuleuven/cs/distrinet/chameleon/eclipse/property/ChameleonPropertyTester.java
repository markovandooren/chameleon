package be.kuleuven.cs.distrinet.chameleon.eclipse.property;

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.view.outline.ChameleonOutlineTree;

/**
 * A default class for testing properties. It extracts the
 * actual receiver objects from collections and then runs
 * more specialized tests.
 * 
 * @author Marko van Dooren
 *
 */
public class ChameleonPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		try {
			if(receiver instanceof Collection) {
				for(Object object: (Collection)receiver) {
					if(! doTest(object, property, args, expectedValue)) {
						return false;
					}
				}
				return true;
			} else {
				return doTest(receiver,property, args, expectedValue);
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean doTest(Object receiver, String property, Object[] args,
			Object expectedValue) {
		boolean result = false;
		if(receiver instanceof ChameleonOutlineTree) {
			ChameleonOutlineTree selection = (ChameleonOutlineTree) receiver;
			result = testChameleonElement(selection.getElement(), property, args, expectedValue);
		} else if(receiver instanceof IProject){
			result = testProject((IProject)receiver, property, args, expectedValue);
		} 
		if(! result) {
			result = testObject(receiver, property, args, expectedValue);
		}
		return result;
	}
	
	/**
	 * The fallback case when none of the other test methods is applicable or when they return false.
	 * @param object
	 * @param property
	 * @param args
	 * @param expectedValue
	 * @return
	 */
	protected boolean testObject(Object object, String property, Object[] args,	Object expectedValue) {
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
	protected boolean testChameleonElement(Element element, String property, Object[] args,	Object expectedValue) {
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
	protected boolean testProject(IProject project, String property, Object[] args,	Object expectedValue) {
		return false;
	}
}
