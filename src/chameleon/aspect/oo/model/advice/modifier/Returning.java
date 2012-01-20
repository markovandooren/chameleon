package chameleon.aspect.oo.model.advice.modifier;


import org.rejuse.property.PropertySet;

import chameleon.aspect.oo.model.advice.property.ReturningProperty;
import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;

/**
 * 	Represents returning advice. Returning advice exposes a single parameter, that can be used in the advice body.
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 *
 * 	@param <E>
 */
public class Returning<E extends Returning<E>> extends ModifierWithParameters<E> {
	
	/**
	 * 	Constructor
	 */
	public Returning() {

	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(ReturningProperty.ID));
	}

	@Override
	protected E cloneThis() {
		return (E) new Returning<E>();
	}
}