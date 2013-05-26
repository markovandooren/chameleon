package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.modifier;


import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.property.ReturningProperty;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

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
