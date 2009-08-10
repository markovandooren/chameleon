package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;

public class SimpleReference<D extends Declaration> extends SpecificReference<SimpleReference,Element,D> {

	public SimpleReference(ElementReference<?, ?, ? extends TargetDeclaration> target, String name, Class<D> specificClass) {
		super(target,name,specificClass);
	}

	public SimpleReference(String fqn, Class<D> specificClass) {
		super(fqn, specificClass);
	}

}
