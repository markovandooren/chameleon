package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

public interface DependencyFilter<E extends Element, T extends Declaration> {

	public boolean acceptsDependency(E element, T target);
}
