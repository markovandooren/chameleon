package org.aikodi.chameleon.core.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.rejuse.logic.ternary.Ternary;
import org.aikodi.rejuse.property.Property;
import org.aikodi.rejuse.property.PropertySet;


/**
 * A convenience class for modifiers. Setting the parent is not done in the constructor
 * because adding a modifier to an element will set up the bidirectional association.
 * 
 * @author Marko van Dooren
 */
public abstract class ModifierImpl extends ElementImpl implements Modifier {

  /**
   * Convenience method for creating an empty propertyset.
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
  
  /**
   * The default implementation return a valid result.
   */
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
	
	@Override
   public Ternary implies(Property property) {
		return impliedProperties().implies(property);
	}

	@Override
   public boolean impliesTrue(Property property) {
		return implies(property) == Ternary.TRUE;
	}

	@Override
   public boolean impliesFalse(Property property) {
		return implies(property) == Ternary.FALSE;
	}

	@Override
   public boolean impliesUnknown(Property property) {
		return implies(property) == Ternary.UNKNOWN;
	}

	@Override
   public Ternary contradicts(Property property) {
		return impliedProperties().contradicts(property);
	}

	@Override
   public boolean contradictsTrue(Property property) {
		return contradicts(property) == Ternary.TRUE;
	}

	@Override
   public boolean contradictsFalse(Property property) {
		return contradicts(property) == Ternary.FALSE;
	}

	@Override
   public boolean contradictsUnknown(Property property) {
		return contradicts(property) == Ternary.UNKNOWN;
	}

  
}
