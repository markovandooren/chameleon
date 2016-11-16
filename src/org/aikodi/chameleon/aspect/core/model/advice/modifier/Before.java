package org.aikodi.chameleon.aspect.core.model.advice.modifier;

import org.aikodi.chameleon.aspect.core.model.advice.property.BeforeProperty;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.property.PropertySet;

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
