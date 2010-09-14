package chameleon.oo.type;

import chameleon.core.lookup.Stub;

public interface TypeElementStub<E extends TypeElementStub<E>> extends Stub<E> {

	public TypeElement generator();
}
