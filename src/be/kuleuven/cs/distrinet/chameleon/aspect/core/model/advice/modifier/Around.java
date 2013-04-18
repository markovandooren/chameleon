package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.modifier;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.property.AroundProperty;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public class Around<E extends Around<E>> extends ModifierImpl {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(AroundProperty.ID));
	}

	@Override
	public E clone() {
		return (E) new Around<E>();
	}

}
