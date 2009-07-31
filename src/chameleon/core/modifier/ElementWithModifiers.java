package chameleon.core.modifier;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElement;

public interface ElementWithModifiers<E extends Element<E, P>, P extends Element> extends NamespaceElement<E,P> {

	public abstract E clone();

	/**
	 * Return the modifiers of this type element.
	 */
 /*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	public abstract List<Modifier> modifiers();

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
	public abstract void addModifier(Modifier modifier);

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
	public abstract void removeModifier(Modifier modifier);

 /*@
	 @ public behavior
	 @
	 @ pre modifiers != null;
	 @
	 @ post modifiers().containsAll(modifiers);
	 @*/
	public abstract void addModifiers(List<Modifier> modifiers);

}