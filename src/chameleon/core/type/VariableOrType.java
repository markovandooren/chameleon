package chameleon.core.type;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Target;
import chameleon.core.namespace.NamespaceElement;


/**
 * @author marko
 */

public interface VariableOrType<E extends VariableOrType<E,P,S>, P extends DeclarationContainer, S extends Signature> extends NamespaceElement<E,P>, Target<E,P>, Declaration<E,P,S> {

	public Type getType() throws LookupException;

}
