package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;

public interface CrossReferenceWithName<D extends Declaration> extends CrossReference<D> {

	public void setName(String name);
	
	public String name();
	

}
