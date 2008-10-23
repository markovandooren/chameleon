package chameleon.core.tag;

import chameleon.core.element.Element;

public interface Tag {

	Element getElement();
	
	void setElement(Element element, String name);

}
