package org.aikodi.chameleon.aspect.oo.model.advice.modifier;


import org.aikodi.chameleon.aspect.oo.model.advice.property.ReturningProperty;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.property.PropertySet;

/**
 * 	Represents returning advice. Returning advice exposes a single parameter, that can be used in the advice body.
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public class Returning extends ModifierWithParameters {
	
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
	protected Returning cloneSelf() {
		return new Returning();
	}
}
