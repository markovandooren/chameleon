package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public interface CrossReferenceWithName<D extends Declaration> extends CrossReference<D> {

	public void setName(String name);
	
	public String name();
	

}
