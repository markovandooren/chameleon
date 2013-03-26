package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public interface CrossReferenceWithTarget<D extends Declaration> extends CrossReference<D> {
	
	public CrossReferenceTarget getTarget();
	
	public void setTarget(CrossReferenceTarget target);

}
