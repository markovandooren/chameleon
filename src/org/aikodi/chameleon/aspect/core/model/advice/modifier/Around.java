package org.aikodi.chameleon.aspect.core.model.advice.modifier;

import org.aikodi.chameleon.aspect.core.model.advice.property.AroundProperty;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.property.PropertySet;

public class Around<E extends Around<E>> extends ModifierImpl {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(AroundProperty.ID));
	}

	@Override
	protected E cloneSelf() {
		return (E) new Around<E>();
	}

}
