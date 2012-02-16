package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.QualifiedName;
import chameleon.core.declaration.SimpleNameSignature;

public class SimpleReference<D extends Declaration> extends SpecificReference<D> {

	public SimpleReference(CrossReferenceTarget target, String name, Class<D> specificClass) {
		super(target,name,specificClass);
	}

	public SimpleReference(CrossReferenceTarget  target, SimpleNameSignature signature, Class<D> specificClass) {
		super(target,signature,specificClass);
	}

	public SimpleReference(QualifiedName name, Class<D> specificClass) {
		super(name, specificClass);
	}

	/**
	 * Initialize a new simple reference given a fully qualified name. The name is split at every dot, and
	 * multiple objects are created to form a chain of references.
	 * @param fqn
	 * @param specificClass
	 */
	public SimpleReference(String fqn, Class<D> specificClass) {
		super(fqn, specificClass);
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	public SimpleReference<D> clone() {
	   return new SimpleReference<D>((getTarget() == null ? null : getTarget().clone()), (SimpleNameSignature)signature().clone(), specificType());
	}


}
