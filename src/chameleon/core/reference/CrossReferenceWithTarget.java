package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

public interface CrossReferenceWithTarget<E extends CrossReferenceWithTarget,P extends Element, D extends Declaration> extends CrossReference<E,P,D> {
	
	//FIXME the return type sucks!
	public Element getTarget();

}
