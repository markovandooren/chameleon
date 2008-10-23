package chameleon.core.namespacepart;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;

public interface NamespacePartElement<E extends Element, P extends Element> extends Element<E,P> {

	//public Namespace getNamespacex();
	
	//public NamespacePart getNamespacePart();
	
	public NamespacePart getNearestNamespacePart();
	
	public CompilationUnit getCompilationUnit();

}
