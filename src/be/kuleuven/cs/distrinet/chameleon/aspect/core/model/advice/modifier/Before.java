package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.modifier;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.property.BeforeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public class Before<E extends Before<E>> extends ModifierImpl {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(BeforeProperty.ID));
	}

	@Override
	protected E cloneSelf() {
		return (E) new Before<E>();
	}

}
