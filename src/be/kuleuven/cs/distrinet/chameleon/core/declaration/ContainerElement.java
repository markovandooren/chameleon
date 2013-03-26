package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public interface ContainerElement extends Element {

	public List<Element> declaredElements() throws LookupException;
}
