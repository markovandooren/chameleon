package chameleon.core.declaration;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public interface ContainerElement extends Element {

	public List<Element> declaredElements() throws LookupException;
}
