package chameleon.aspect.core.model.advice.modifier;

import org.rejuse.property.PropertySet;

import chameleon.aspect.core.model.advice.property.AroundProperty;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.support.modifier.PublicProperty;

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
