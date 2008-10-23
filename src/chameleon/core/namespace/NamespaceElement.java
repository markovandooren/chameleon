package chameleon.core.namespace;

import chameleon.core.element.Element;

public interface NamespaceElement<E extends Element, P extends Element> extends Element<E,P> {
  
	public Namespace getNamespace();
	
}
