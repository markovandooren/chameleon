package org.aikodi.chameleon.aspect.core.model.advice.modifier;

import org.aikodi.chameleon.aspect.core.model.advice.property.AfterProperty;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.property.PropertySet;

public class After extends ModifierImpl {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(AfterProperty.ID));
	}

	@Override
	protected After cloneSelf() {
		return new After();
	}
}
