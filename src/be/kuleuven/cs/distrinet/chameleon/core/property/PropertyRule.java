package be.kuleuven.cs.distrinet.chameleon.core.property;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.language.WrongLanguageException;
import be.kuleuven.cs.distrinet.chameleon.core.rule.Rule;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * A class for assigning default properties to model elements. Default properties are
 * properties that an element has if no property of a certain kind has been defined explicitly.
 * Thus, when the default properties are consulted, those that conflict with explicitly declared
 * properties are ignored.
 * 
 * @author Marko van Dooren
 */
public abstract class PropertyRule<E extends Element> extends Rule<PropertyRule, E> {

	public PropertyRule(Class<E> elementType) {
		super(elementType);
	}

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
	public PropertySet<Element,ChameleonProperty> properties(E element) {
		if(appliesTo(element)) {
		  PropertySet<Element,ChameleonProperty> suggested = suggestedProperties(element);
		  PropertySet<Element,ChameleonProperty> declared = element.declaredProperties();
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
	public final boolean appliesTo(Element element) {
		return elementType().isInstance(element);
	}

 /*@
   @ public behavior
   @
   @ pre appliesTo(element);
   @
   @ post \result != null; 
   @*/
	public abstract PropertySet<Element,ChameleonProperty> suggestedProperties(E element);
	
	// COPIED FROM MODIFIERIMPL
	
  /**
   * Convenience method for creating an empty propertyset
   */
  protected PropertySet<Element,ChameleonProperty> createSet() {
    return new PropertySet<Element,ChameleonProperty>(); 
  }
  
  /**
   * Convenience method for creating a propertyset with a single element.
   */
  protected PropertySet<Element,ChameleonProperty> createSet(ChameleonProperty p) {
    PropertySet<Element,ChameleonProperty> result = createSet();
    result.add(p);
    return result;
  }

  /**
   * Convenience method for creating a propertyset with two elements.
   */
  protected PropertySet<Element,ChameleonProperty> createSet(ChameleonProperty p1, ChameleonProperty p2) {
  	PropertySet<Element,ChameleonProperty> result = createSet(p1);
    result.add(p2);
    return result;
  }
  
  /**
   * Convenience method for creating a propertyset with three elements.
   */
  protected PropertySet<Element,ChameleonProperty> createSet(ChameleonProperty p1, ChameleonProperty p2, ChameleonProperty p3) {
  	PropertySet<Element,ChameleonProperty> result = createSet(p1, p2);
    result.add(p3);
    return result;
  }

	
	// END COPIED FROM MODIFIERIMPL
}
