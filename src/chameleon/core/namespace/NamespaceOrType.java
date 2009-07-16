package chameleon.core.namespace;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.Target;

/**
 * @author Marko van Dooren
 */

public interface NamespaceOrType<E extends NamespaceOrType<E,P,S,F>, P extends DeclarationContainer, S extends Signature, F extends Declaration> extends Target<E,P>, Element<E,P>, Declaration<E,P,S,F>, DeclarationContainer<E,P> {

	public String getFullyQualifiedName();
  
}
