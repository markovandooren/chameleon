package chameleon.oo.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Target;
import chameleon.core.namespace.NamespaceElement;


/**
 * @author Marko van Dooren
 */

public interface VariableOrType<E extends VariableOrType<E,P,S,F>, P extends Element, S extends Signature, F extends Declaration> extends NamespaceElement<E,Element>, Target<E,Element>, Declaration<E,Element,S,F> {

	public Type getType() throws LookupException;

}
