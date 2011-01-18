package chameleon.core.modifier;

import java.util.List;

import org.rejuse.property.PropertyMutex;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElement;
import chameleon.exception.ModelException;

/**
 * This is a convenience class for element that can have modifiers. Elements of this class automatically use the modifiers
 * to determine their declared properties.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 */
public interface ElementWithModifiers<E extends Element<E, P>, P extends Element> extends NamespaceElement<E,P> {

	public E clone();

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
	
	public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException; 

}