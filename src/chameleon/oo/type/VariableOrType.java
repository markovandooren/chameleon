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

public interface VariableOrType<E extends VariableOrType<E,S,F>, S extends Signature, F extends Declaration> extends NamespaceElement<E>, Target<E>, Declaration<E,S,F> {

	public Type getType() throws LookupException;

}
