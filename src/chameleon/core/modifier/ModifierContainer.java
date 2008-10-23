package chameleon.core.modifier;

import java.util.List;

import chameleon.core.element.Element;

public interface ModifierContainer<E extends Element, P extends Element> extends Element<E,P> {
	
	public List<Modifier> modifiers();
}
