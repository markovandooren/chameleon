package chameleon.aspect.core.model.advice.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import chameleon.aspect.core.model.advice.property.AfterProperty;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;

public class After extends ModifierImpl {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(AfterProperty.ID));
	}

	@Override
	public After clone() {
		return new After();
	}
}
