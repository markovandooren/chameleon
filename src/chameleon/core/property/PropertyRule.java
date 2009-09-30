package chameleon.core.property;

import org.rejuse.association.SingleAssociation;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.language.WrongLanguageException;
import chameleon.core.rule.Rule;

/**
 * A class for assigning default properties to model elements. Default properties are
 * properties that an element has if no property of a certain kind has been defined explicitly.
 * Thus, when the default properties are consulted, those that conflict with explicitly declared
 * properties are ignored.
 * 
 * @author Marko van Dooren
 */
public abstract class PropertyRule extends Rule<PropertyRule> {

	/**
	 * Return the default properties of the given element by this assigner. The result
	 * is computed by taking the properties suggested by this element, and then removing
	 * those properties that contradict one of the explicitly declared properties of that
	 * element.
	 * 
	 * @param element
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post \result == suggested.withoutContradictingProperties(declared);
   @*/
	public PropertySet<Element> properties(Element element) {
		if(appliesTo(element)) {
		  PropertySet<Element> suggested = suggestedProperties(element);
		  PropertySet<Element> declared = element.declaredProperties();
		  return suggested.withoutContradictingProperties(declared);
		} else {
			return createSet();
		}
	}
	
 /*@
   @ public behavior
   @
   @ post element == null ==> \result == false;
   @*/
	public abstract boolean appliesTo(Element element);

 /*@
   @ public behavior
   @
   @ pre appliesTo(element);
   @
   @ post \result != null; 
   @*/
	public abstract PropertySet<Element> suggestedProperties(Element element);
	
	// COPIED FROM MODIFIERIMPL
	
  /**
   * Convenience method for creating an empty propertyset
   */
  protected PropertySet<Element> createSet() {
    return new PropertySet<Element>(); 
  }
  
  /**
   * Convenience method for creating a propertyset with a single element.
   */
  protected PropertySet<Element> createSet(Property p) {
    PropertySet<Element> result = createSet();
    result.add(p);
    return result;
  }

  /**
   * Convenience method for creating a propertyset with two elements.
   */
  protected PropertySet<Element> createSet(Property p1, Property p2) {
  	PropertySet<Element> result = createSet(p1);
    result.add(p2);
    return result;
  }
  
  /**
   * Convenience method for creating a propertyset with three elements.
   */
  protected PropertySet<Element> createSet(Property p1, Property p2, Property p3) {
  	PropertySet<Element> result = createSet(p1, p2);
    result.add(p3);
    return result;
  }

	
	// END COPIED FROM MODIFIERIMPL
}
