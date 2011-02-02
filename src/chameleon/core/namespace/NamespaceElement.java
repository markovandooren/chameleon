package chameleon.core.namespace;

import chameleon.core.element.Element;

public interface NamespaceElement<E extends Element> extends Element<E> {
  
	public Namespace getNamespace();
	
}
