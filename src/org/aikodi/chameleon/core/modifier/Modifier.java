package org.aikodi.chameleon.core.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.logic.ternary.Ternary;
import org.aikodi.rejuse.property.Property;
import org.aikodi.rejuse.property.PropertySet;


/**
 * A modifier is an element that gives properties to another element.
 * @author Marko van Dooren
 */
public interface Modifier extends Element {

	/**
	 * Return the set of properties that are implied by this modifier.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null; 
   @*/
  public PropertySet<Element,ChameleonProperty> impliedProperties();
  
  /**
   * Check if this modifier implies the given property. The result is a ternary value.
   */
 /*@
   @ public behavior
   @
   @ post \result == impliedProperties().implies(property);
   @*/
	public Ternary implies(Property property);

	/**
   * Check if this modifier implies the given property. Note that
   * impliesTrue and contradictsFalse are not the same because of the
   * ternary logic. UNKNOWN will cause both methods to return false.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.TRUE; 
   @*/
	public boolean impliesTrue(Property property);

	/**
   * Check if this modifier does not imply the given property. Note that
   * impliesFalse and contradictsTrue are not the same because of the
   * ternary logic. UNKNOWN will cause both methods to return false.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.FALSE; 
   @*/
	public boolean impliesFalse(Property property);

	/**
   * Check if it is unknown whether this modifier implies the given property. Note that
   * impliesUnknown and contradictsUnknown ARE the same.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.UNKNOWN; 
   @*/
	public boolean impliesUnknown(Property property);

  /**
   * Check if this modifier contradicts the given property. The result is a ternary value.
   */
 /*@
   @ public behavior
   @
   @ post \result == impliedProperties().contradicts(property);
   @*/
	public Ternary contradicts(Property property);

	/**
   * Check if this modifier contradicts the given property. Note that
   * contradictsTrue and impliesFalse are not the same because of the
   * ternary logic. UNKNOWN will cause both methods to return false.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.TRUE; 
   @*/
	public boolean contradictsTrue(Property property);

	/**
   * Check if this modifier does not contradict the given property. Note that
   * contradictsFalse and impliesTrue are not the same because of the
   * ternary logic. UNKNOWN will cause both methods to return false.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.FALSE; 
   @*/
	public boolean contradictsFalse(Property property);

	/**
   * Check if it is unknown whether this modifier contradicts the given property. Note that
   * contradictsUnknown and impliesUnknown ARE the same.
   */
 /*@
   @ public behavior
   @
   @ post \result == implies(property) == Ternary.UNKNOWN; 
   @*/
	public boolean contradictsUnknown(Property property);

}
