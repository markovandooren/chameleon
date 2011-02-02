package chameleon.core.declaration;

import java.util.List;

import chameleon.core.namespace.NamespaceElementImpl;

public abstract class QualifiedName<E extends QualifiedName> extends NamespaceElementImpl<E> {

	public abstract List<Signature> signatures();
	
	public abstract Signature lastSignature();
	
	public abstract E clone();
	
	public abstract int length();
	
	/**
	 * Return the index-th signature. Indices start at 1.
	 * @param index
	 * @return
	 */
	public abstract Signature elementAt(int index);
	
	/**
	 * Return a new qualified name that contains all signatures of this qualified name, except for the last one.
	 * @return
	 */
	public QualifiedName<?> popped() {
		CompositeQualifiedName<?> result = new CompositeQualifiedName();
		List<Signature> signatures = signatures();
		int length = signatures.size();
		for(int i=0; i< length-1; i++) {
			result.append(signatures.get(i).clone());
		}
		return result;
	}
	
}
