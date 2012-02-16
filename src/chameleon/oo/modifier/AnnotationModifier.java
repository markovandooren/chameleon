package chameleon.oo.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;

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
