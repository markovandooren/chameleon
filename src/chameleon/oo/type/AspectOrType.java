package chameleon.oo.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.namespace.NamespaceOrType;

public interface AspectOrType<E extends AspectOrType<E, D>, D extends Declaration> extends NamespaceElement<E,Element>, Declaration<E, Element,  SimpleNameSignature, D> {

	/********
	 * NAME *
	 ********/

	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @*/
	public String getName();


}
