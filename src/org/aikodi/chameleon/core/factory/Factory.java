package org.aikodi.chameleon.core.factory;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespace.RootNamespaceReference;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;

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
		return createNamespaceDeclaration(createNameReference(fqn, Namespace.class));
	}

	/**
	 * Create a new name reference for an element of the given type with the given
	 * fully qualified name.
	 * 
	 * @param fqn The fully qualified name of the referenced declaration.
	 * @param type A class object representing the type of the referenced declaration.
	 * @return a name reference for an element of the given type with the given
    * fully qualified name.
	 */
   public <D extends Declaration> NameReference<D> createNameReference(String fqn, Class<D> type) {
      return new NameReference<>(fqn, type);
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
