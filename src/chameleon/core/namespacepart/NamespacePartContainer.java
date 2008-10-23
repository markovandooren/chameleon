package chameleon.core.namespacepart;

import java.util.List;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public interface NamespacePartContainer<E extends Element, P extends Element> extends Element<E,P> {
	
	public List<NamespacePart> getNamespaceParts();
	
	//@fixme : is this method necessary?
	public CompilationUnit getCompilationUnit();
}
