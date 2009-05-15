package chameleon.core.property;

import java.util.Set;

import org.rejuse.property.Property;

import chameleon.core.element.Element;

/**
 * A class for assigning default properties to model elements. Default properties are
 * properties that an element has if no property of the same PropertyMutex has been defined explicitly.
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class PropertyFactory {

	/**
	 * Return the default properties of the given element
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
	public abstract Set<Property> defaultProperties(Element element);
	
}
