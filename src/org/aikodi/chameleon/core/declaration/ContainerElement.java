package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public interface ContainerElement extends Element {

	public List<Element> declaredElements() throws LookupException;
}
