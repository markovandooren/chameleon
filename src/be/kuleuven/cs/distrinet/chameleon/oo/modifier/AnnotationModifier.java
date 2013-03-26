package be.kuleuven.cs.distrinet.chameleon.oo.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;

public class AnnotationModifier extends ModifierImpl {

	public AnnotationModifier(String name) {
		setName(name);
	}
	
	@Override
	public AnnotationModifier clone() {
		return new AnnotationModifier(name());
	}

	private String _name;
	
	public String name() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}

	public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return new PropertySet<Element, ChameleonProperty>();
	}
}
