package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;

public interface Namespace extends TargetDeclaration, DeclarationContainer, SimpleNameDeclaration {

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
	 * Return all namespace parts attached to this namespace. All unloaded input sources
	 * will be loaded.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<NamespaceDeclaration> getNamespaceParts();
	
	public List<NamespaceDeclaration> loadedNamespaceParts();

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
	@Deprecated
   public void addNamespacePart(NamespaceDeclaration namespacePart);
	
	public List<Namespace> getSubNamespaces();

	public List<Namespace> getAllSubNamespaces();

	public boolean hasSubNamespaces();
	
	public Namespace getSubNamespace(final String name) throws LookupException;
	
	public NamespaceAlias alias(String name);
	
	public Namespace createSubNamespace(String name);
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) throws LookupException;
}
