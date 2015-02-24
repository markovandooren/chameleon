package be.kuleuven.cs.distrinet.chameleon.core.factory;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RootNamespaceReference;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.ExpressionFactory;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePluginImpl;

/**
 * A factory class for object oriented languages.
 * 
 * @author Marko van Dooren
 */
public class Factory extends LanguagePluginImpl {

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
		return createNamespaceDeclaration(language().plugin(ExpressionFactory.class).createSimpleReference(fqn, Namespace.class));
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
	
	@Override
	public Factory clone() {
	   return new Factory();
	}
}
