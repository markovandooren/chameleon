package org.aikodi.chameleon.core.element;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.ElementImpl.Navigator;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;
import org.aikodi.chameleon.core.event.association.ChildReplaced;
import org.aikodi.chameleon.core.event.association.ParentAdded;
import org.aikodi.chameleon.core.event.association.ParentRemoved;
import org.aikodi.chameleon.core.event.association.ParentReplaced;
import org.aikodi.chameleon.core.event.stream.EventStream;
import org.aikodi.chameleon.core.event.stream.EventStreamCollection;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.language.WrongLanguageException;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.CrossReferenceImpl;
import org.aikodi.chameleon.core.tag.Metadata;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.ChameleonAssociation;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.chameleon.workspace.WrongViewException;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.IAssociation;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.logic.ternary.Ternary;
import org.aikodi.rejuse.property.Property;
import org.aikodi.rejuse.property.PropertySet;

/**
 * <p>
 * An interface for language constructs.
 * </p>
 * 
 * <p>
 * Element is the top interface for an element of a source model. Every source
 * element (every language construct that can be part of the "source code") must
 * implement this interface. On top of that, every class for a language
 * construct must encapsulate the semantics of that element. As a result, tools
 * no longer have to contain the base semantics of a language, making it much
 * easier to reuse the tool for models of a different language.
 * </p>
 * 
 * <h3>Design</h3>
 *
 * <p>
 * This is a large interface, but it focuses on a the key responsibilities that
 * any language construct has. Removing responsibilities will only make the
 * framework less powerful. Many methods for the lexical structure, though,
 * could be moved to the {@link #lexical()} structure object, but that makes it
 * harder to use the functionality. Writing element.lexical().parent() is not as
 * convenient as element.parent().
 * </p>
 * 
 * <h3>The Lexical Structure</h3>
 * 
 * <embed src="lexicalStructure.svg"/>
 * 
 * <p>
 * Every Element provides many methods to navigate the lexical structure of the
 * model through methods to access the children, descendants, and ancestors. The
 * lexical structure can be navigated in any direction: from outer elements to
 * inner elements or vice versa. By default, the {@link ElementImpl#children()}
 * method collects all objects referenced by {@link ChameleonAssociation}
 * fields.
 * </p>
 * 
 * <p>
 * If one of these {@link ChameleonAssociation} fields does not reference
 * lexical children, you can exclude it by writing the following code. Suppose
 * that <code>C</code> is the name of the class that contains the field.
 * </p>
 * 
 * 
 * 
 * <code>
 * class C ... {
 *   Association _fieldName;
 *   
 *   static {
 *     excludeFieldName(C.class, "_fieldName");
 *   }
 * }
 * </code>
 * 
 * <h3>Namespaces</h3>
 * 
 * <embed src="namespaceStructure.svg"/>
 * 
 * <p>
 * Every top-level {@link Declaration}, for example a top-level class in Java,
 * is logically part of a {@link Namespace}. Because namespaces are not
 * explicitly defined, they are not part of the lexical structure. A declaration
 * is added to a namespace by putting it in a {@link NamespaceDeclaration} that
 * is linked to the namespace. A namespace declaration, which is similar to a
 * package in Java, is part of the lexical structure. If a language does not
 * support explicit namespaces, it can simply put everything in the root name
 * space.
 * </p>
 * <p>
 * The root namespace is connected to a {@link View}, which contains all model
 * elements of a particular language. Each project can have multiple views. For
 * now, lookups across views are not supported yet. They should be supported in
 * the future.
 * </p>
 * 
 * <h3>The logical structure</h3>
 * 
 * <p>
 * The logical structures within a model are modeled indirectly, instead of
 * through direct object references. See the interface
 * {@link chameleon.core.reference.CrossReference} for the explanation.
 * </p>
 * 
 * <h3>Properties</h3>
 * 
 * <p>
 * Each element can have {@link Property}s. The properties typically come from
 * three sources:
 * </p>
 * <ol>
 * <li>Properties that are inherent to the language construct:
 * {@link #inherentProperties()}</li>
 * <li>Properties that are explicitly added to an element:
 * {@link #declaredProperties()}. Typically these properties are added to an
 * element by {@link Modifier}s.</li>
 * <li>Properties that are added to a language construct by the {@link Language}
 * . Typically, these are default rules, which are used when a kind of property
 * is not inherent to a language construct or set explicitly.</li>
 * </ol>
 * 
 * <p>
 * The default implementation of {@link #properties()} in {@link ElementImpl}
 * combines these three source correctly, so you have to worry only about the
 * individual sources of properties.
 * </p>
 * 
 * <h3>Verification</h3>
 * 
 * <p>
 * Each language construct is responsible for verifying itself via the
 * {@link #verify()} method. In practice, a language construct only has to
 * implement implement the {@link #verifySelf()} method to verify itself.
 * 
 * <p>
 * The default implementation of {@link #verify()} takes care of the recursive
 * descent such that language constructs have to verify only themselves. In
 * addition, it also checks whether the set of properties of an element is
 * consistent. For example, this makes it very easy to add modifiers without
 * having to worry whether an element has a valid combination of modifiers. A
 * declaration that is both final and abstract will report a verification
 * problem because they modifiers assign conflicting properties to the same
 * element.
 * </p>
 * 
 * <p>
 * In addition, many abstractions in the framework provide powerful verification
 * rules. For example, every {@link CrossReference} will automatically check if
 * it can be resolved, if the class inherits from {@link CrossReferenceImpl}.
 * </p>
 *
 * <h3>Events</h3>
 * <p>
 * Elements in the model can send {@link Event}s when they are modified. For all
 * changes involving the {@link #associations()} (and thus the tree structure),
 * events are sent automatically. For other changes, the subclasses must send
 * the events themselves, typically by calling
 * {@link ElementImpl#notify(org.aikodi.chameleon.core.event.Change)}
 * </p>
 * <p>
 * When the parent of an element is changed, it sends the following events to
 * the {@link ElementEventStreamCollection#self()} stream of its even stream
 * collection {@link #when()}.
 * </p>
 * <ol>
 * <li>{@link ParentAdded}</li>
 * <li>{@link ParentRemoved}</li>
 * <li>{@link ParentReplaced}</li>
 * </ol>
 * 
 * <p>
 * When a child of an element is changed, it sends the following events to the
 * {@link ElementEventStreamCollection#self()} stream of its even stream
 * collection {@link #when()}.
 * </p>
 * <ol>
 * <li>{@link ChildAdded}</li>
 * <li>{@link ChildRemoved}</li>
 * <li>{@link ChildReplaced}</li>
 * </ol>
 * 
 * <p>
 * To listen to events originating from this element, execute the following
 * code:
 * </p>
 * <code>
 *  element.when().self().call(e -> event handling code);
 * </code>
 * <p>
 * To listen to events from descendants of this element, execute the following
 * code:
 * </p>
 * <code>
 *  element.when().descendant().call(e -> event handling code);
 * </code>
 * <p>
 * To listen to events from either this element or its descendants, execute the
 * following code:
 * </p>
 * <code>
 *  element.when().any().call(e -> event handling code);
 * </code>
 * <p>
 * Do not forget to detach the listener via
 * {@link EventStream#stopCalling(org.aikodi.chameleon.core.event.EventListener)}
 * if you want it to be garbage collected. You need to store a reference to the
 * lambda if you want to do this. Alternatively, when {@link #disconnect()} is
 * invoked, all listeners are detached automatically.
 * </p>
 * 
 * <p>
 * See class {@link EventStream} for options to filter the event stream, and
 * receive only certain events.
 * </p>
 * 
 * <h3>Metadata</h3>
 * 
 * <p>
 * Every element can have metadata associated with it. This can be used to
 * attach additional information to an element without adding dependencies to
 * Chameleon.
 * </p>
 * 
 * @author Marko van Dooren
 * 
 */
/*
 @startuml lexicalStructure.svg
 left to right direction
 object project
 object view
 object documentScanner
 object documentLoader
 object document
 object namespaceDeclaration
 object declaration
 object namespace

 project -- view
 view -- documentScanner
 documentScanner -- documentLoader
 documentLoader -- document
 document -- namespaceDeclaration
 namespaceDeclaration -- declaration
 namespaceDeclaration - namespace
 @enduml

 @startuml namespaceStructure.svg
 left to right direction
 object "root namespace" as rootNamespace
 object "a" as a
 object "b" as b
 object "c" as c
 object "c.a" as ca
 object "c.b" as cb
 object namespaceDeclaration
 object document

 project -- view
 view -- rootNamespace
 rootNamespace -- a 
 rootNamespace -- b 
 rootNamespace -- c 
 c -- ca
 c -- cb
 cb - namespaceDeclaration
 document -- namespaceDeclaration
 @enduml
 */
public interface Element extends Cloneable {

  /**
   * @return a tree structure for the lexical structure of this element. This
   * tree stops at the {@link Document} level.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Navigator<Nothing> lexical();

  /**
   * @return a tree structure for the logical structure of this element. This
   * includes the {@link Namespace} structure.
   */
  public Navigator<LookupException> logical();

  /**
   * Return the object representing the <b>bidirectional</b>link to the parent of this element.
   * 
   * This link is <b>NOT</b> used for elements that are derived/generated! Always use parent() to obtain
   * the parent.
   */
  public SingleAssociation<? extends Element,? extends Element> parentLink();

  /**
   * <p>Freeze this element. This locks the parent links of all descendants.
   * As a result, no descendant can be removed 
   * and no additional descendant can be added anywhere in the subtree. A {@link org.aikodi.rejuse.association.LockException}
   * is thrown if such a modification is attempted.
   * The parent link of this element, however, is not modified. If it was locked, it will be still be locked
   * afterwards. If it was unlocked, it will be still be unlocked afterwards. </p> 
   * 
   * <p>This method is useful for debugging and for ensuring that e.g. predefined elements
   * and elements that correspond to "binary" document cannot be modified by accident.</p> 
   */
  public default void freeze() {
		for(Element element: lexical().children()) {
			element.parentLink().lock();
			element.freeze();
		}
		for(IAssociation association: associations()) {
			association.lock();
		}
  }

  /**
   * <p>Unfreeze this element. This means that descendants can again be removed 
   * and added anywhere in the subtree.
   * The parent link of this element, however, is not modified. If it was locked, it will be still be locked
   * afterwards. If it was unlocked, it will be still be unlocked afterwards. </p> 
   */
  public default void unfreeze() {
		for(Element element: lexical().children()) {
			element.parentLink().unlock();
			element.unfreeze();
		}
		for(ChameleonAssociation<?> association: associations()) {
			association.unlock();
		}
  }

  /**
   * <p>Completely disconnect this element and all descendants from the parent.
   * This method also removes associations with any <b>logical</b> parents. All
   * event streams of this element are disconnected.</p>
   */
 /*@
   @ public behavior
   @
   @ post disconnected();
   @ post (\forall Element e; \old(this.contains(e)); e.parent() == null);
   @*/
  public void disconnect();

  /**
   * Return the objects representing the associations with the lexical children of this element.
   * The default implementation uses reflection to obtain the list of associations. 
   * If a language construct uses additional association objects that are not part of the lexical structure,
   * they should be excluded using the static excludeFieldName method in ElementImpl. 
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List<ChameleonAssociation<?>> associations();

  /**
   * Check if this element is derived (or generated) or not. A derived element has a unidirectional 
   * connection with its parent.
   * 
   * @return True if this element is derived, false otherwise.
   */
 /*@
   @ public behavior
   @
   @ post \result == (! (origin() == this));
   @*/
  public boolean isDerived();

  /**
   * If this element is derived, return the element from which this element originates.
   * If this element is not derived, the method returns the current object.
   */
  /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
  public Element origin();

  /**
   * Return the fartest origin of this element. The farthest origin of an element is
   * the fartest origin of its origin. The farthest origin of an element that has itself
   * as its origin is the element itself.
   */
  /*@
     @ public behavior
     @
     @ post origin() == this ==> \result == this;
     @ post origin() != this ==> \result == origin().farthestOrigin();
     @*/
  public default Element farthestOrigin() {
		Element current = this;
		Element origin = origin();
		while(current != origin) {
			current = origin;
			origin = current.origin();
		}
		return origin;
  }

  /**
   * Set the origin of this element.
   * 
   * @param element The new origin of this element.
   */
  /*@
     @ public behavior
     @
     @ post origin() == element;
     @*/
  public void setOrigin(Element element);


  /**
   * Return the metadata with the given key.
   * @param key
   *        The key under which the metadata is registered.
   */
  public Metadata metadata(String key);

  /**
   * Return all metadata associated with this element.
   */
  /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Metadata data;; 
     @        \result.contains(data) == (\exists String key;; metadata(key) == data));
     @*/
  public Collection<Metadata> metadata();

  /**
   * Check whether or not metadata is registered under the given key
   * @param name
   *        The name to be checked
   * @return
   */
  /*@
     @ public behavior
     @
     @ pre key != null;
     @
     @ post \result == (metadata(key) != null);
     @*/
  public boolean hasMetadata(String key);

  /**
   * Register the given metadata under the given key. 
   * 
   * If an element was already registered under the given key, 
   * that element will be unregistered and its element reference set to null 
   * such that the bidirectional association is kept consistent.
   * 
   * @param data
   *        The data to be registered
   * @param key
   *        The key under which the given metadata must be registered,
   */
 /*@
   @ public behavior
   @
   @ pre key != null;
   @ pre data != null;
   @
   @ post metadata(key) == data;
   @*/
  public void setMetadata(Metadata data, String key);

  /**
   * Remove the metadata registered under the given key.
   * @param key
   *        The key of the metadata to be removed.
   */
 /*@
   @ public behavior
   @
   @ post tag(key) == null;
   @*/
  public void removeMetadata(String key);

  /**
   * Remove all metadata from this element
   */
 /*@
   @ public behavior
   @
   @ post metadata().isEmpty();
   @*/
  public void removeAllMetadata();

  /**
   * Return the language of this element. Return null if this element is not
   * connected to a complete model.
   */
 /*@
   @ public behavior
   @
   @ post (view() == null) ==> (\result == null);
   @ post (view() != null) ==> (\result.equals(view().language()))
   @*/
  public default Language language() {
	  View view = view();
		return view == null ? null : view.language();
  }

  /**
   * Return the language of this element if it is of the wrong kind. Return null if this element is not
   * connected to a complete model. Throws a WrongLanguageException is the
   * language is not of the correct type.
   */
  /*@
     @ public behavior
     @
     @ pre kind != null;
     @
     @ post \result == language();
     @
     @ signals(WrongLanguageException) language() != null && ! kind.isInstance(language());
     @*/
  public <T extends Language> T language(Class<T> kind) throws WrongLanguageException;

  /**
   * Return the project to which this element belongs. Return null if the element is not part of a project.
   * @return
   */
  /*@
     @ public behavior
     @
     @ post (view() == null) ==> (\result == null);
     @ post (view() != null) ==> (\result == view().project());
     @*/
  public default Project project() {
		return view().project();
	}

  /**
   * Return the view of this element. Return null if this element is not connected
   * to a complete model. The call is delegated to the parent until is reaches the document
   * which is always the outmost lexical element. From there, the call proceeds through the
   * document loading infrastructure to obtain the view.
   * @return
   */
  /*@
     @ public behavior
     @
     @ post (parent() == null) ==> (\result == null);
     @ post (parent() != null) ==> (\result == parent().view());
     @*/
  public View view();

  /**
   * Return the view of this element if it is of the wrong kind. Return null if this element is not
   * connected to a complete model. Throws a WrongViewException is the
   * language is not of the correct type.
   */
  /*@
     @ public behavior
     @
     @ pre kind != null;
     @
     @ post \result == view();
     @
     @ signals(WrongViewException) view() != null && ! kind.isInstance(view());
     @*/
  public default <T extends View> T view(Class<T> kind) throws WrongViewException {
		if(kind == null) {
			throw new ChameleonProgrammerException("The given language class is null.");
		}
		View view = view();
		if(kind.isInstance(view) || view == null) {
			return (T) view;
		} else {
			throw new WrongViewException("The view of this element is of the wrong kind. Expected: "+kind.getName()+" but got: " +view.getClass().getName());
		}
  }

  /**
   * Return a deep clone of this element. The returned element has no parent. 
   * @return
   */
  /*@
     @ public behavior
     @
     @ post \result != null;
     @ post \result.getParent() == null;
     @ post \result.children().size() == children().size();
     @*/
  public Element clone();

  /**
   * Clone the given element. This is a convenience method.
   * This method performs an unsafe cast! The return type
   * of {@Link #clone()} is {@link Element}. Using a type
   * parameters as the self type is too cumbersome in Java.
   * 
   * @param element The element to be cloned.
   * @return A clone of the given element.
   */
  /*@
     @ public behavior
     @
     @ post \result == clone();
     @*/
  public default <T extends Element> T clone(T element) {
    // Without the cast, the clone call is bound to the
    // clone of Object, which throws a CloneNotSupportedException.
    // If you write the same code in ElementImpl, the cast is not required. 
    // This seems like a bug in the JDT.
    return (T) ((Element)element).clone();
  }

  /**
   * Clone this element and apply the given biconsumer to corresponding
   * elements in the lexical structure of both this element and the resulting
   * clone if they are of the given type.
   *  
   * @param consumer The consumer that will process the corresponding elements 
   * in both element trees.
   * @param type The type of element to which the consumer must be applied.
   * @return A clone of this element that has been processed by the given
   * consumer.
   */
  public <E extends Element> Element clone(final BiConsumer<E, E> consumer, Class<E> type);

  /**
   * Return the lexical lookup context for the given child element. The
   * default behavior is to delegate the search to the parent.
   * 
   * If this element contains declarations itself, it searches those
   * elements first, and only delegates to the parent if nothing
   * is found.
   * 
   * @param element
   *        The child element of this element for which the
   *        context is requested
   * @return The lookup context for the given child.
   * @throws LookupException The lookup context could not be retreived because
   *                         of an inconsistency in the model.
   */
  /*@
     @ public behavior
     @
     @ pre children().contains(child);
     @
     @ post \result != null; 
     @*/
  public default LookupContext lookupContext(Element child) throws LookupException {
    return lexicalContext();
  }

  /**
   * Return the lexical context for this element. By default, this
   * call will be delegated to the parent by invoking {@link #lookupContext(Element)}
   * with parameter 'this' on the parent.
   * 
   * @return The lexical lookup context of this element.
   */
  /*@
     @ public behavior
     @
     @ post \result == parent().lexicalContext(this);
     @
     @ signals (MetamodelException) parent() == null; 
     @*/
  public LookupContext lexicalContext() throws LookupException;

  /**
   * DO NOT USE THIS METHOD UNLESS YOU REALLY KNOW WHAT YOU ARE DOING!!!
   * 
   * Create a unidirectional parent association to the given element. If the given parent is not null,
   * the parentLink() association object will be set to null and a unidirectional reference to the
   * given parent is used. If the given parent is null, then the parentLink() association will be restored
   * but it will of course not yet reference any element. 
   *
   * This method is used internally for generated elements. They are not part
   * of the model, but need a parent in order to have a context. Unfortunately,
   * Java does not allow me to hide the method.
   * 
   * @param parent The new parent of this element.
   */
  /*@
     @ public behavior
     @
     @ post parent() == parent;
     @ post parent == null ==> parentLink() != null;
     @ post parent != null ==> parentLink() == null;
     @*/
  public void setUniParent(Element parent);

  /**************
   * PROPERTIES *
   **************/

  /**
   * Return the set of properties of this element. The set consists of the
   * explicitly declared properties, and the default properties for which
   * no corresponding explicitly declared property exists.
   */
  /*@
     @ public behavior
     @
     @ post \result != null;
     @ post \result.containsAll(declaredProperties());
     @ post \result.containsAll(language().properties(this));
     @ post (\forall Property<Element> p; \result.contains(p);
     @         declaredProperties().contains(p) |
     @         language().defaultProperties(this).contains(p));
     @*/
  public abstract PropertySet<Element,ChameleonProperty> properties();

  /**
   * Return a property set representing the properties of this element
   * that are explicitly declared.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public default PropertySet<Element,ChameleonProperty> declaredProperties() {
    return new PropertySet<Element,ChameleonProperty>();
  }

  /**
   * Check if this type element has the given property. 
   * 
   * If the given property does not apply directly to this type element, we check if it
   * is implied by the declared properties of this element. If the given property does apply
   * to this type element, we still need to check for conflicts with the declared properties.
   * 
   * @param property
   *        The property to be verified.
   */
 /*@
   @ behavior
   @
   @ pre property != null;
   @
   @ post ! property.appliesTo(this) ==> \result == declaredProperties().implies(property);
   @ post property.appliesTo(this) ==> \result == declaredProperties().with(property).implies(property);
   @*/
  public Ternary is(ChameleonProperty property);


  /**
   * WARNING: THIS IS TERNARY LOGIC!!! For example, it is not the case that !isTrue(p) == isFalse(p).
   * 
   * Check if this type element really has the given property. 
   * 
   * @param property
   *        The property to be verified.
   */
  /*@
     @ behavior
     @
     @ pre property != null;
     @
     @ post \result == (is(property) == Ternary.TRUE);
     @*/
  public boolean isTrue(ChameleonProperty property);

  /**
   * WARNING: THIS IS TERNARY LOGIC!!! For example, it is not the case that !isFalse(p) == isTrue(p).
   *
   * Check if this type element really does not have the given property. 
   * 
   * @param property
   *        The property to be verified.
   */
  /*@
     @ behavior
     @
     @ pre property != null;
     @
     @ post \result == (is(property) == Ternary.FALSE);
     @*/
  public boolean isFalse(ChameleonProperty property);

  /**
   * WARNING: THIS IS TERNARY LOGIC!!! For example, it is not the case that !isUnknown(p) == isTrue(p).
   * 
   * Check if it is unknown whether or not this type element has the given property. 
   * 
   * @param property
   *        The property to be verified.
   */
  /*@
     @ behavior
     @
     @ pre property != null;
     @
     @ post \result == (is(property) == Ternary.UNKNOWN);
     @*/
  public boolean isUnknown(ChameleonProperty property);

  /**
   * @return A verification object that indicates whether or not this is valid,
   *         and if not, what the problems are. The result includes the problems
   *         found in all descendants.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Verification verify();

  /*************************
   * IDENTITY and EQUALITY *
   *************************/

  /**
   * Because equals cannot throw checked exceptions, we have some framework code
   * for equality.
   * 
   * First of all: DO NOT USE EQUALS for elements!!! Use sameAs(Element) instead.
   * The latter method can throw a ModelException whereas equals does not allow it.
   * 
   * To ensure that equals work correctly when it is invoked by third-party code
   * (e.g. java.util.HashSet), equals is implemented in terms of sameAs, which
   * in turn is implemented as uniSameAs. The latter method checks if an object
   * states that it is equal to another (similar to equals). The sameAs method
   * invokes this method on both objects to check if one of both objects claims
   * to be the same as the other.
   * 
   * <b>The default behavior is the same as that of {@link Object#equals(Object)}.</b>
   * 
   * <b>Do not forget to override {@link Object#hashCode()} if you override {@link #uniSameAs(Element)}.</b>
   */
 /*@
   @ public behavior
   @
   @ post (other instanceof Element) && sameAs((Element)other);
   @
   @ signals (LookupExceptionInEquals) (* sameAs(other) has thrown an LookupException *);
   @*/
  @Override 
  public boolean equals(Object other);

  /**
   * Check if this element is the same as the other element.
   */
  /*@
     @ public behavior
     @
     @ post other == null ==> \result == false;
     @ post other == this ==> \result == true;
     @ post other != null ==> \result == uniSameAs(other) || other.uniSameAs(this);
     @*/
  public boolean sameAs(Element other) throws LookupException;

  /**
   * Check whether this element is the same as the other element.
   * This is the method that needs to be implemented in subclasses.
   * The {@link #sameAs(Element)} method ensures that equality is idempotent
   * and symmetric.
   */
  /*@
     @ public behavior
     @
     @ post other == null ==> \result == false;
     @*/
  public boolean uniSameAs(Element other) throws LookupException;

  /**
   * Return the namespace of this element.
   * 
   * By default, it returns the namespace of the nearest namespace declaration.
   * @return
   */
  public default Namespace namespace() {
  	NamespaceDeclaration namespaceDeclaration = lexical().nearestAncestor(NamespaceDeclaration.class);
		return namespaceDeclaration != null ? namespaceDeclaration.namespace() : null;
  }

  /**
   * Flush any caching this element may have.
   * This method flushes the local cache using "flushLocalCache()" and then
   * recurses into the children.
   */
  public void flushCache();

  /**
   * <p>
   * Return the event stream collection of this element. See
   * {@link EventStreamCollection} for documentation about the possible sources
   * of events.
   * </p>
   * <p>The event stream collection is created on-demand and removed
   * when all listeners have disconnected.
   * </p>
   * @return the event stream collection of this element. The result is not null.
   */
  public ElementEventStreamCollection when();

  /**
   * Replace this element with the given other element. WARNING: this operation
   * is not type safe. You may end up inserting an element of the wrong type
   * into the model.
   * 
   * @param replacement
   */
  public default void replaceWith(Element replacement) {
    if(isDerived()) {
      throw new ChameleonProgrammerException("A derived element cannot be replaced.");
    }
    parentLink().getOtherRelation().replace((Association)parentLink(), (Association)replacement.parentLink());
  }
  
}
