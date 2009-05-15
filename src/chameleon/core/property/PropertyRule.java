package chameleon.core.property;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;

/**
 * A class for assigning default properties to model elements. Default properties are
 * properties that an element has if no property of the same PropertyMutex has been defined explicitly.
 * 
 * @author Marko van Dooren
 */
public abstract class PropertyRule {

	/**
	 * Return the default properties of the given element by this assigner
	 * @param element
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post \result != null;
   @*/
	public abstract PropertySet<Element> properties(Element element);
	
}
