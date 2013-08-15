package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public interface DependencyFilter<E extends Element, T extends Declaration> {

	public boolean acceptsDependency(E element, T target);
}
