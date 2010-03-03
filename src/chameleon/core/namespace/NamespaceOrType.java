package chameleon.core.namespace;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.Target;

/**
 * @author Marko van Dooren
 */

public interface NamespaceOrType<E extends NamespaceOrType<E,P,S,F>, P extends Element, S extends Signature, F extends Declaration> extends Target<E,Element>, Element<E,Element>, TargetDeclaration<E,Element,S,F>, DeclarationContainer<E,Element> {

	public String getFullyQualifiedName();
	
	public LookupStrategy localStrategy() throws LookupException;
  
}
