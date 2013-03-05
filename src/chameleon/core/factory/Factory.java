package chameleon.core.factory;

import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.RootNamespaceReference;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SimpleReference;
import chameleon.plugin.LanguagePluginImpl;

/**
 * A factory class for object oriented languages.
 * 
 * @author Marko van Dooren
 */
public abstract class Factory extends LanguagePluginImpl {

	/**
	 * Create a new namespace declaration for the given namespace.
	 *
	 * @param ns The namespace in which the namespace declaration will add elements.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre ns != null;
   @ post \result != null;
   @ post \result.namespace() == ns;
   @*/
	public NamespaceDeclaration createNamespaceDeclaration(String fqn) {
		return createNamespaceDeclaration(new SimpleReference<Namespace>(fqn, Namespace.class));
	}
	
	/**
	 * Create a new namespace declaration for the given namespace.
	 *
	 * @param ns The namespace in which the namespace declaration will add elements.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre ns != null;
   @ post \result != null;
   @ post \result.namespace() == ns;
   @*/
	public NamespaceDeclaration createNamespaceDeclaration(CrossReference<Namespace> cref) {
		return new NamespaceDeclaration(cref);
	}
	
	/**
	 * Create a new namespace declaration for the root namespace.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.namespace() instanceof RootNamespaceReference;
   @*/
	public NamespaceDeclaration createRootNamespaceDeclaration() {
		return createNamespaceDeclaration(new RootNamespaceReference());
	}
}
