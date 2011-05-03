package chameleon.core.namespace;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.Target;

/**
 * @author Marko van Dooren
 */

public interface NamespaceOrType<E extends NamespaceOrType<E,S>, S extends Signature> extends TargetDeclaration<E,S>, DeclarationContainer<E> {

	public String getFullyQualifiedName();
	
	public LookupStrategy localStrategy() throws LookupException;
  
}
