package chameleon.core.namespacepart;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceElement;

public abstract class NamespaceElementImpl<E extends Element, P extends Element> extends ElementImpl<E,P> implements NamespaceElement<E,P> {

	public Namespace getNamespace() {
		NamespacePart ancestor = nearestAncestor(NamespacePart.class);
		if(ancestor != null) {
		  return ancestor.getDeclaredNamespace();
		} else {
			return null;
		}
	}
	
//	public Language language() {
//	  Namespace ns = getNamespace();
//	  if(ns != null) {
//		  return getNamespace().language();
//	  } else {
//	    return null;
//	  }
//	}
}
