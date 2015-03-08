package org.aikodi.chameleon.core.namespace;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.reference.CrossReference;

/**
 * An interface for namespaces that contain the declarations of a view/project.
 * 
 * @author Marko van Dooren
 */
public interface Namespace extends TargetDeclaration, DeclarationContainer {

	/**
	 * Return the fully qualified name of this package. This is the concatenation of the
	 * parent namespaces starting at the root. In between the names of two namespaces, a
	 * "." character is placed.
	 */
 /*@
	 @ public behavior
	 @
	 @ post (getParent() == null) || getParent().getName().equals("") ==>
	 @        \result == getName();
	 @ post (getParent() != null) && (! getParent().getName().equals("")) ==>
	 @        \result == getParent().getFullyQualifiedName() + "." + getName();
	 @*/
	public String fullyQualifiedName();
	
	/**
	 * Return the root namespace of this metamodel instance.
	 */
 /*@
	 @ public behavior
	 @
	 @ post getParent() != null ==> \result == getParent().defaultNamespace();
	 @ post getParent() == null ==> \result == this;
	 @*/
	public Namespace defaultNamespace();
	
	/**
	 * <p>Return the namespace with the fullyqualified name that
	 * equals the fqn of this namespace concatenated with the
	 * given name.</p>
	 *
	 * <p>If the namespace does not exist yet, it will be created.</p>
	 *
	 * @param qualifiedName
	 *        The qualified name relative to this namespace
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post (! this == defaultNamespace()) ==> \result.getFullyQualifiedName().equals(getFullyQualifiedName() + "." + name);
   @ post (this == defaultNamespace()) ==> \result.getFullyQualifiedName().equals(name);
   @*/
	public Namespace getOrCreateNamespace(final String name);
	
  /**
   * The name of a namespace is the name of its signature.
   */
 /*@
   @ public behavior
   @
   @ post \result == signature().getName();
   @*/
	@Override
   public String name();
	
	/**
	 * Return all namespace parts attached to this namespace. 
	 * All unloaded documents will be loaded.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<NamespaceDeclaration> namespaceDeclarations();
	
	/**
	 * Return all namespace declarations that are currently loaded.
	 * @return 
	 */
	public List<NamespaceDeclaration> loadedNamespaceDeclarations();

	/**
	 * <B>DO NOT INVOKE</B> it is an internal method that must be public because
	 * it is in an interface.
	 * 
	 * Add a namespace part to this namespace. A namespace part adds elements to its namespace.
	 * 
	 * @deprecated This method must be removed from this interface now that the lexical structure is
	 *             built upon the project scanners.
	 */
 /*@
   @ public behavior
   @
   @ pre namespacepart != null;
   @
   @ post getNamespaceParts().contains(namespacepart);
   @*/
	@Deprecated
   public void addNamespacePart(NamespaceDeclaration namespacePart);
	
	/**
	 * @return All direct subnamespaces of this namespace.
	 */
	public List<Namespace> subNamespaces();

   /**
    * @return All direct subnamespaces of this namespace, and the
    * descendants of these subnamespaces.
    */
	public List<Namespace> descendantNamespaces();

	/**
	 * @return True if this namespace has subnamespaces, false otherwise.
	 */
	public boolean hasSubNamespaces();
	
	/**
	 * Return the subnamespace with the given name.
	 * 
	 * @param name The name of the requested subnamespace
	 * @return  the subnamespace with the given name.
	 * @throws LookupException The namespace could not be found.
	 */
	public Namespace getSubNamespace(final String name) throws LookupException;
	
	public NamespaceAlias alias(String name);
	
	/**
	 * Create and return a subnamespace with the given name.
	 * 
	 * @param name The name of the subnamespace to be created.
	 * @return a subnamespace with the given name.
	 */
	public Namespace createSubNamespace(String name);
	
	/**
	 * A convenience method for finding the element of the given type with the given 
	 * qualified name.
	 * 
	 * @param qualifiedName The qualified name of the requested declaration <b>relative</b>
	 *                      to this namespace.
	 * @param type The type of the requested declaration.
	 * @return A declaration with the given qualified name.
	 * @throws LookupException The element could not be uniquely identified. It may
	 * not exists, there may be multiple candidates,...
	 */
   public default <D extends Declaration> D find(String qualifiedName, Class<D> type) throws LookupException {
      CrossReference<D> cref = language().plugin(Factory.class).createNameReference(qualifiedName, type);
      cref.setUniParent(this);
      return cref.getElement();
   }

}
