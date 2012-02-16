package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

public interface CrossReferenceWithName<D extends Declaration> extends CrossReference<D> {

	public void setName(String name);
	
	public String name();
	

}
