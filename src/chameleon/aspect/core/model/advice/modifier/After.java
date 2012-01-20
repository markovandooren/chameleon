package chameleon.aspect.core.model.advice.modifier;

import org.rejuse.property.PropertySet;

import chameleon.aspect.core.model.advice.property.AfterProperty;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;

public class After<E extends After<E>> extends ModifierImpl<E> implements Modifier<E> {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(AfterProperty.ID));
	}

	@Override
	public E clone() {
		return (E) new After();
	}
}
