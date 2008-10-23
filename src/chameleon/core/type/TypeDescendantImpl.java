package chameleon.core.type;

import chameleon.core.element.Element;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacepart.NamespacePartElementImpl;

public abstract class TypeDescendantImpl<E extends Element,P extends Element> extends NamespacePartElementImpl<E,P> implements TypeDescendant<E,P> {

	public TypeDescendantImpl() {
	}
	
	public final Namespace getNamespace() {
		return getNearestNamespacePart().getDeclaredNamespace();
	}

}
