package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;

public interface CrossReferenceWithTarget<D extends Declaration> extends CrossReference<D> {
	
	public CrossReferenceTarget getTarget();
	
	public void setTarget(CrossReferenceTarget target);

}
