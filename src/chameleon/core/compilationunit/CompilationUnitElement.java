package chameleon.core.compilationunit;

import chameleon.core.element.Element;

public interface CompilationUnitElement<E extends Element, P extends Element> extends Element<E,P> {

	public CompilationUnit getCompilationUnit();

}
