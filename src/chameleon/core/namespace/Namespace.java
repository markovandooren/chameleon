package chameleon.core.namespace;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;

public interface Namespace extends Declaration, DeclarationContainer {

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
	public String getFullyQualifiedName();
	
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
	 * Recursively search for all declarations of a given kind. This will also search in child namespaces
	 * @param kind
	 * @return
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.containsAll(declarations(kind));
   @ post (\forall Namespace ns; getSubnamespaces().contains(ns); \result.containsAll(ns.allDecendantDeclarations(kind)));
   @*/
	public <T extends Declaration> List<T> allDescendantDeclarations(Class<T> kind) throws LookupException;
	
	public Namespace clone();
	
  /**
   * The name of a namespace is the name of its signature.
   */
 /*@
   @ public behavior
   @
   @ post \result == signature().getName();
   @*/
	public String name();
	
	/**
	 * Return all namespace parts attached to this namespace.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<NamespaceDeclaration> getNamespaceParts();
	
	/**
	 * Add a namespace part to this namespace. A namespace part adds elements to its namespace.
	 * 
	 * @deprecated This method must be remove from this interface now that the lexical structure is
	 *             built upon the project loaders.
	 */
 /*@
   @ public behavior
   @
   @ pre namespacepart != null;
   @
   @ post getNamespaceParts().contains(namespacepart);
   @*/
	public void addNamespacePart(NamespaceDeclaration namespacePart);
	
	public List<Namespace> getSubNamespaces();
	
	public Namespace getSubNamespace(final String name) throws LookupException;
	
	public NamespaceAlias alias(SimpleNameSignature sig);
	
	public Namespace createSubNamespace(String name);
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) throws LookupException;
}
