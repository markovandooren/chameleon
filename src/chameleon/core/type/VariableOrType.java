package chameleon.core.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Target;
import chameleon.core.namespace.NamespaceElement;


/**
 * @author Marko van Dooren
 */

public interface VariableOrType<E extends VariableOrType<E,P,S,F>, P extends DeclarationContainer, S extends Signature, F extends Declaration> extends NamespaceElement<E,P>, Target<E,P>, Declaration<E,P,S,F> {

	public Type getType() throws LookupException;

}
