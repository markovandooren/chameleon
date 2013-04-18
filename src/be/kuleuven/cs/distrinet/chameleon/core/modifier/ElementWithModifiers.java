package be.kuleuven.cs.distrinet.chameleon.core.modifier;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

/**
 * This is a convenience class for element that can have modifiers. Elements of this class automatically use the modifiers
 * to determine their declared properties.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface ElementWithModifiers extends Element {

	public ElementWithModifiers clone();

	/**
	 * Return the modifiers of this type element.
	 */
 /*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	public List<Modifier> modifiers();

	/**
	 * Add the given modifier to this type element.
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
	 */
 /*@
	 @ public behavior
	 @
	 @ pre modifier != null;
	 @
	 @ post ! modifiers().contains(modifier);
	 @*/
	public void removeModifier(Modifier modifier);

 /*@
	 @ public behavior
	 @
	 @ pre modifiers != null;
	 @
	 @ post modifiers().containsAll(modifiers);
	 @*/
	public void addModifiers(List<Modifier> modifiers);
	
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
	public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException;
	
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
	public List<Modifier> modifiers(Property property) throws ModelException;

}
