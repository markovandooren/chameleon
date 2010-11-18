package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

public interface CrossReferenceWithName<E extends CrossReferenceWithName,P extends Element, D extends Declaration> extends CrossReference<E,P,D> {

	public void setName(String name);
	
	public String name();
	

}
