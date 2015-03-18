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
 * <p>An interface for namespaces that contain the declarations of a view/project.</p>
 * 
 * <embed src="class.svg"/>
 * 
 * <h3>Subnamespaces</h3>
 * 
 * <p>Each namespace can have subnamespaces. The fully qualified name
 * of a namespace is its own {@link #name()} prefixed by the {@link #fullyQualifiedName()}
 * of its {@link #parent()} namespace.</p>
 * 
 * <embed src="namespace-hierarchy.svg"/>
 * 
 * <h3>Declarations</h3>
 * 
 * <p>{@link Declaration}s are not stored directly in a namespace. Instead,
 * every declaration is part of a {@link NamespaceDeclaration}. This
 * is similar to a package declaration in Java, and a namespace declaration
 * in C#. The declarations in the namespace declarations are aggregated
 * in the {@link #declarations()} of a namespace.</p>
 * 
 * <embed src="namespace-declaration.svg"/>
 * 
 * @author Marko van Dooren
 */
/*green #F0FFE4
  blue #F1FAFF -> CFE7FF -> DBEDFF
 @startuml class.svg
 
 interface Element
 interface Declaration
 interface DeclarationContainer
 interface Namespace {
   +fullyQualifiedName()
   +find(String,Class)
   +name()
   ..subnamespaces..
   +getOrCreateNamespace(String)
   +getSubnamespace(String)
 }
 Element <|-- Namespace
 Declaration <|-- Namespace
 DeclarationContainer <|-- Namespace
 
 @enduml

 *
 *
 *@startuml namespace-hierarchy.svg
 * object root
 * object a {
 *   name = "a"
 * }
 * object b {
 *   name = "b"
 * }
 * object c {
 *   name = "c"
 * }
 * object "a.a" as aa {
 *   name = "a"
 * }
 * object "a.b" as ab {
 *   name = "b"
 * }
 * object "a.c" as ac {
 *   name = "c"
 * }
 * object "c.a" as ca {
 *   name = "a"
 * }
 * object "c.b" as cb {
 *   name = "b"
 * }
 * root -- a
 * root -- b
 * root -- c
 * a -- aa
 * a -- ab
 * a -- ac
 * c -- ca
 * c -- cb
 *@enduml
 *@startuml namespace-declaration.svg
 * left to right direction
 * !definelong nsd(number)
 * object document##number
 * object namespaceDeclaration##number
 * object "declaration 1" as a##number
 * object "declaration ..." as b##number
 * object "declaration n" as c##number
 * document##number -- namespaceDeclaration##number
 * namespaceDeclaration##number -- a##number
 * namespaceDeclaration##number -- b##number
 * namespaceDeclaration##number -- c##number
 * !enddefinelong 
 *
 * object namespace
 * 
 * nsd(1)
 * nsd(2)
 * nsd(3)
 * 
 * namespace -- namespaceDeclaration1
 * namespace -- namespaceDeclaration2
 * namespace -- namespaceDeclaration3
 *@enduml
 * namespace -- namespaceDeclaration1
 * document1 - namespaceDeclaration1
 * namespaceDeclaration1 - declaration1a
 * namespaceDeclaration1 - declaration1b
 * namespace -- namespaceDeclaration2
 * document2 - namespaceDeclaration2
 * namespaceDeclaration2 - declaration2a
 * namespaceDeclaration2 - declaration2b
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
   * Return all namespace declarations in this namespace that are currently
   * loaded. This method does not load any namespace declaration itself.
   * 
   * @return all namespace declarations in this namespace that are currently
   * loaded
   */
	public List<NamespaceDeclaration> loadedNamespaceDeclarations();

	/**
	 * <B>DO NOT INVOKE</B> it is an internal method that must be public because
	 * it is in an interface.
	 * 
	 * Add a namespace part to this namespace. A namespace part adds elements to its namespace.
	 * 
	 * @deprecated This method should be called only from within {@link NamespaceDeclaration}.
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
   * Return all descendant namespaces. Calling this method is much more
   * efficient than calling descendants(Namespace.class) because the latter will
   * search within the attached {@link NamespaceDeclaration}s as well, where
   * there can be no namespace objects that are part of the main namespace
   * structure.
   * 
   * @return All direct subnamespaces of this namespace, and the descendants of
   *         these subnamespaces.
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
