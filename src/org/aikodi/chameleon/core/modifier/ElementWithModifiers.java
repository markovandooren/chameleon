package org.aikodi.chameleon.core.modifier;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.contract.Contract;
import org.aikodi.rejuse.property.PropertyMutex;
import org.aikodi.rejuse.property.PropertySet;

/**
 * An element that can have modifiers. Elements of this class automatically use 
 * the modifiers to determine their declared properties.
 * 
 * @author Marko van Dooren
 */
public interface ElementWithModifiers extends Element {

	/**
	 * @return the modifiers of this type element. The result is not null
	 * and does not contain a null reference.
	 */
 /*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	public List<Modifier> modifiers();

	/**
	 * Add the given modifier to this type element.
	 * 
	 * @param modifier The modifier to be added. The modifier cannot be null.
	 */
 /*@
	 @ public behavior
	 @
	 @ pre modifier != null;
	 @
	 @ post modifiers().contains(modifier);
   @*/
	public void addModifier(Modifier modifier);

	/**
	 * Remove the given modifier from this type element.
	 * 
	 * @param modifier The modifier to be removed.
	 */
 /*@
	 @ public behavior
	 @
	 @ pre modifier != null;
	 @
	 @ post ! modifiers().contains(modifier);
	 @*/
	public void removeModifier(Modifier modifier);

  /**
   * Add all modifiers in the given collection to this element.
   * 
   * @param modifiers The collection that contains the modifiers that must be added.
   */
 /*@
	 @ public behavior
	 @
	 @ pre modifiers != null;
	 @
	 @ post modifiers().containsAll(modifiers);
	 @*/
	public default void addModifiers(List<Modifier> modifiers) {
	  Contract.requireNotNull(modifiers, "The given list cannot be null");
	  modifiers.forEach(m -> addModifier(m));
	}
	
	/**
	 * Return all modifiers that implies properties that are in the given mutex.
	 */
 /*@
   @ public behavior
   @
   @ post (\forall Modifier modifier: \result.contains(modifier) : modifiers().contains(modifier));
   @ post (\forall Modifier modifier
   @             : modifiers().contains(modifier) && 
   @                                 (\exists Property property
   @                                        : modifier.impliedProperties().contains(property)
   @                                        : propery.mutex() == mutex)
   @             : \result.contains(modifier));
   @*/
	public List<Modifier> modifiers(PropertyMutex<ChameleonProperty> mutex) throws ModelException;
	
	/**
	 * Return all modifiers that imply the given property.
	 */
 /*@
   @ public behavior
   @
   @ post (\forall Modifier modifier: \result.contains(modifier) : modifiers().contains(modifier));
   @ post (\forall Modifier modifier
   @             : modifiers().contains(modifier) && modifier.impliedProperties().contains(property)
   @             : \result.contains(modifier));
   @*/
	public default List<Modifier> modifiers(ChameleonProperty property) throws ModelException {
	  List<Modifier> result = Lists.create();
	  for(Modifier modifier: modifiers()) {
	    if(modifier.impliesTrue(property)) {
	      result.add(modifier);
	    }
	  }
	  return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return For every modifier, its {@link Modifier#impliedProperties()} are
	 * added to the result.
	 */
	@Override
	public default PropertySet<Element,ChameleonProperty> declaredProperties() {
	  PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
	  for(Modifier modifier:modifiers()) {
	    result.addAll(modifier.impliedProperties().properties());
	  }
	  return result;
	}
}
