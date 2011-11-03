package chameleon.core.reference;

import chameleon.core.declaration.Declaration;

public interface CrossReferenceWithTarget<E extends CrossReferenceWithTarget,D extends Declaration> extends CrossReference<E,D> {
	
	public CrossReferenceTarget<?> getTarget();
	
	public void setTarget(CrossReferenceTarget<?> target);

}
