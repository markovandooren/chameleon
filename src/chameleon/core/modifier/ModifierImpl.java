package chameleon.core.modifier;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;


/**
 * A convenience class for modifiers. Setting the parent is not done in the constructor
 * because adding a modifier to an element will set up the bidirectional association.
 * 
 * @author Marko van Dooren
 */
public abstract class ModifierImpl extends ElementImpl implements Modifier {



  public ModifierImpl() {
  }
 
  /**
   * Clone this modifier.
   */
  public abstract ModifierImpl clone();

  /**
   * By default, a modifier has no children, but subclasses are allowed to
   * change this behavior. Therefore, you cannot rely on children() returning
   * an empty set.
   */
  public List<? extends Element> children() {
    return new ArrayList<Element>();
  }

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
  
	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
	
	public Ternary implies(Property property) {
		return impliedProperties().implies(property);
	}

	public boolean impliesTrue(Property property) {
		return implies(property) == Ternary.TRUE;
	}

	public boolean impliesFalse(Property property) {
		return implies(property) == Ternary.FALSE;
	}

	public boolean impliesUnknown(Property property) {
		return implies(property) == Ternary.UNKNOWN;
	}

	public Ternary contradicts(Property property) {
		return impliedProperties().contradicts(property);
	}

	public boolean contradictsTrue(Property property) {
		return contradicts(property) == Ternary.TRUE;
	}

	public boolean contradictsFalse(Property property) {
		return contradicts(property) == Ternary.FALSE;
	}

	public boolean contradictsUnknown(Property property) {
		return contradicts(property) == Ternary.UNKNOWN;
	}

  
}
