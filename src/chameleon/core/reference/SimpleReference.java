package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;

public class SimpleReference<D extends Declaration> extends SpecificReference<SimpleReference<D>,Element,D> {

	public SimpleReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name, Class<D> specificClass) {
		super(target,name,specificClass);
	}

	public SimpleReference(String fqn, Class<D> specificClass) {
		super(fqn, specificClass);
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	public SimpleReference<D> clone() {
	   return new SimpleReference<D>((getTarget() == null ? null : getTarget().clone()), getName(), specificType());
	}

}
