package chameleon.aspect.core.model.advice.modifier;

import org.rejuse.property.PropertySet;

import chameleon.aspect.core.model.advice.property.BeforeProperty;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;

public class Before<E extends Before<E>> extends ModifierImpl<E> implements Modifier<E> {

	@Override
	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return createSet(language().property(BeforeProperty.ID));
	}

	@Override
	public E clone() {
		return (E) new Before<E>();
	}

}
