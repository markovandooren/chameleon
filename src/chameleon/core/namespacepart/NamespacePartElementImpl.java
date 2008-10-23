package chameleon.core.namespacepart;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.namespace.Namespace;

public abstract class NamespacePartElementImpl<E extends Element, P extends Element> extends ElementImpl<E,P> implements NamespacePartElement<E,P> {

	public NamespacePart getNearestNamespacePart() {
		if(getParent() instanceof NamespacePartElement) {
		  return ((NamespacePartElement)getParent()).getNearestNamespacePart();
		} else {
			throw new RuntimeException("Searching nearest NamespacePart while the parent is not a NamespacePartElement");
		}
	}
	
	public Namespace getNamespace() {
		return getNearestNamespacePart().getDeclaredNamespace();
	}
	
	public CompilationUnit getCompilationUnit() {
		return getNearestNamespacePart().getCompilationUnit();
	}
	
	public Language language() {
	  Namespace ns = getNamespace();
	  if(ns != null) {
		  return getNamespace().language();
	  } else {
	    return null;
	  }
	}
}
