package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

public interface CrossReferenceWithName<E extends CrossReferenceWithName,D extends Declaration> extends CrossReference<E,D> {

	public void setName(String name);
	
	public String name();
	

}
