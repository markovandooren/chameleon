package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.QualifiedName;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;

public class SimpleReference<D extends Declaration> extends SpecificReference<SimpleReference<D>,Element,D> {

	public SimpleReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name, Class<D> specificClass) {
		super(target,name,specificClass);
	}

	public SimpleReference(CrossReference<?, ?, ? extends TargetDeclaration> target, Signature signature, Class<D> specificClass) {
		super(target,signature,specificClass);
	}

	public SimpleReference(QualifiedName<?, ?> name, Class<D> specificClass) {
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
	   return new SimpleReference<D>((getTarget() == null ? null : getTarget().clone()), signature().clone(), specificType());
	}


}
