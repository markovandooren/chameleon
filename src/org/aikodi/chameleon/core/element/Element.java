package org.aikodi.chameleon.core.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.language.WrongLanguageException;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.CrossReferenceImpl;
import org.aikodi.chameleon.core.tag.Metadata;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.association.ChameleonAssociation;
import org.aikodi.chameleon.workspace.Project;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.chameleon.workspace.WrongViewException;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * <p>An interface for language constructs.</p>
 * 
 * <p>Element is the top interface for an element of a source model. Every source
 * element (every language construct that can be part of the "source code")
 * must implement this interface. On top of that, every class for a language
 * construct must encapsulate the semantics of that element. As a result, tools
 * no longer have to contain the base semantics of a language, making it much
 * easier to reuse the tool for models of a different language.</p>
 * 
 * <h3>Design</h3>
 *
 * <p>This is a large interface, but it focuses on a few key responsibilities
 * that any language construct has. Removing responsibilities will only
 * make the framework less powerful. Many methods for the lexical structure,
 * though, could be moved to the {@link #lexical()} structure object, but
 * that will either make it harder to use the functionality, or slower, or
 * both. Writing element.lexical().parent() is not as convenient as element.parent().</p>
 * 
 * <h3>The Lexical Structure</h3> 
 * 
 * <p>Every Element provides many methods to
 * navigate the lexical structure of the model through methods to access the
 * children, descendants, and ancestors. The lexical structure can be navigated
 * in any direction: from outer elements to inner elements or vice versa. By
 * default, the {@link ElementImpl#children()} method collects all objects
 * referenced by {@link ChameleonAssociation} fields.</p>
 * 
 * <p>If one of these {@link ChameleonAssociation} fields does not reference
 * lexical children, you can exclude it by writing the following code. Suppose
 * that <code>C</code> is the name of the class that contains the field.</p>
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
 * <h3>The logical structure</h3> 
 * 
 * <p>The logical structures within a model are
 * typically modeled indirectly, instead of through direct object references.
 * See the interface {@link chameleon.core.reference.CrossReference} for the
 * explanation.</p>
 * 
 * <h3>Properties</h3>
 * 
 * <p>Each element can have properties. The properties typically come from
 * three sources:</p>
 * <ol>
 *   <li>Properties that are inherent to the language construct: {@link #inherentProperties()}</li>
 *   <li>Properties that are explicitly added to an element: {@link #declaredProperties()}.
 *   Typically these properties are added to an element by {@link Modifier}s.</li>
 *   <li>Properties that are added to a language construct by the {@link Language}.
 *   Typically, these are default rules, which are used when a kind of property
 *   is not inherent to a language construct or set explicitly.</li>
 * </ol> 
 * 
 * <p>The default implementation of {@link #properties()} in {@link ElementImpl} 
 * combines these three source correctly, so you have to worry only about the
 * individual sources of properties.</p>
 * 
 * <h3>Verification</h3>
 * 
 * <p>Each language construct is responsible for verifying itself. It must
 * implement the {@link #verify()} method to verify itself and its descendants.</p>
 * 
 * <p>The default implementation in {@link ElementImpl} takes care of the
 * recursive descent such that language constructs have to verify only
 * themselves. In addition, it also checks whether the set of properties of
 * an element is consistent. For example, this makes it very easy to add modifiers 
 * without having to worry whether an element has a valid combination of modifiers.
 * A declaration that is both final and abstract will report a verification 
 * problem because they modifiers assign conflicting properties to the same
 * element.</p> 
 * 
 * <p>In addition, many abstractions in the framework provide powerful verification
 * rules. For example, every {@link CrossReference} will automatically check
 * if it can be resolved, if the class inherits from {@link CrossReferenceImpl}.</p>
 *
 * <h3>Metadata</h3>
 * 
 * <p>Every element can have metadata associated with it. This can be used to
 * attach additional information to an element without adding dependencies to
 * Chameleon.</p>
 * 
 * @author Marko van Dooren
 * 
 */
public interface Element {

    /**
     * Return the parent element of this element. Null if there is no parent.
     */
    public Element parent();
    
    /**
     * @return a tree structure for the lexical structure of this element. This
     * tree stops at the {@link Document} level.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public Navigator lexical();
    
    /**
     * @return a tree structure for the logical structure of this element. This
     * includes the {@link Namespace} structure.
     */
    public Navigator logical();
    
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
     * and no additional descendant can be added anywhere in the subtree. A {@link be.kuleuven.cs.distrinet.rejuse.association.LockException}
     * is thrown if such a modification is attempted.
     * The parent link of this element, however, is not modified. If it was locked, it will be still be locked
     * afterwards. If it was unlocked, it will be still be unlocked afterwards. </p> 
     * 
     * <p>This method is useful for debugging and for ensuring that e.g. predefined elements
     * and elements that correspond to "binary" document cannot be modified by accident.</p> 
     */
    public void freeze();
    
    /**
     * <p>Unfreeze this element. This means that descendants can again be removed 
     * and added anywhere in the subtree.
     * The parent link of this element, however, is not modified. If it was locked, it will be still be locked
     * afterwards. If it was unlocked, it will be still be unlocked afterwards. </p> 
     */
    public void unfreeze();
    
    /**
     * Completely disconnect this element and all descendants from the parent.
     * This method also removes associations with any logical parents.
     */
   /*@
     @ public behavior
     @
     @ post disconnected();
     @ post (\forall Element e; \old(this.contains(e)); e.disconnected());
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
     * Check if this element is disconnected. Usually this is true if the
     * lexical parent is null. If an element has additional logical parents,
     * this method must be overridden to verify if the element is also attached
     * from its logical parents. An example of such an element is a namespace part,
     * which is connected to a namespace.
     * 
     * @return
     */
   /*@
     @ public behavior
     @
     @ post \result == true ==> parent() == null;
     @*/
    public boolean disconnected();
    
    /**
     * Completely disconnect all children from this element.
     */
   /*@
     @ public behavior
     @
     @ post (\forall Element e; \old(this.contains(e)); e.disconnected());
     @*/
    public void disconnectChildren();

    /**
     * Completely disconnect this element and all children from the parent.
     * This method also removes associations with any logical parents.
     * 
     * If an element has additional logical parents, this method must be overridden 
     * to remove its references to its logical parents.
     */
   /*@
     @ public behavior
     @
     @ post disconnected();
     @ post (\forall Element e; \old(this.contains(e)); e.disconnected());
     @*/
    public void nonRecursiveDisconnect();

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
    public Element farthestOrigin();

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
     * Return a list of all ancestors. The direct parent is in front of the list, the
     * furthest ancestor is last.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null ==> \result.get(0) == parent();
     @ post parent() != null ==> \result.subList(1,\result.size()).equals(parent().ancestors());
     @*/
    public List<Element> ancestors();

    /**
     * Return a list of all ancestors of the given type. A closer ancestors will have a lower index than a 
     * farther ancestor.
     * 
     * @param c The kind of the ancestors that should be returned.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null && c.isInstance(parent()) ==> \result.get(0) == parent()
     @                       && \result.subList(1,\result.size()).equals(parent().ancestors(c));
     @ post post parent() != null && ! c.isInstance(parent()) ==> \result.equals(parent().ancestors(c));
     @*/
    public <T extends Element> List<T> ancestors(Class<T> c);
    
    /**
     * Return a list of all ancestors of the given type. A closer ancestors will have a lower index than a 
     * farther ancestor.
     * 
     * @param predicate A predicate that determines which ancestors should be returned.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null && predicate.eval(parent()) ==> \result.get(0) == parent()
     @                       && \result.subList(1,\result.size()).equals(parent().ancestors(c));
     @ post parent() != null && ! predicate.eval(parent()) ==> 
     @                       \result.equals(parent().ancestors(c));
     @*/
    public <T extends Element, E extends Exception> List<T> ancestors(UniversalPredicate<T, E> predicate) throws E;
    
    /**
     * Return the direct children of this element.
     * 
     * The result will never be null. All elements in the collection will have this element as
     * their parent.
     * 
     * Note that there can exist non-child elements that have this element as their parent. 
     * The reason is that e.g. not all generic instances of a class can be constructed, so the collection
     * can never be complete anyway. Context elements are also not counted as children, there are merely a
     * help for the lookup algorithms. We only keep references to the lexical children, those that are 'physically'
     * part of the program.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Element e; \result.contains(e); e.parent() == this);
     @*/
    public List<? extends Element> children();

    /**
     * Return all children of this element that are of the given type.
     * 
     * @param c The kind of the children that should be returned.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> children().contains(e) && c.isInstance(e));
     @*/
    public <T extends Element> List<T> children(Class<T> c);
    
    /**
     * Return all children of this element that satisfy the given predicate.
     * 
     * @param predicate A predicate that determines which children should be returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> children().contains(e) && predicate.eval(e) == true);
     @*/
    public <E extends Exception> List<Element> children(Predicate<? super Element,E> predicate) throws E;
    
    /**
     * Return all children of this element that satisfy the given predicate.
     * 
     * @param type The type of the children to which the predicate must be applied.
     * @param predicate A predicate that determines which children should be returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> children().contains(e) && predicate.eval(e) == true);
     @*/
    public default <T, E extends Exception> List<T> children(Class<T> type, Predicate<T,E> predicate) throws E {
      List<? extends Element> children = children();
      List<T> result = new ArrayList<T>(children.size());
      for(Element child: children) {
        if(type.isInstance(child) && predicate.eval((T) child)) {
          result.add((T) child);
        }
      }
      return result;
    }

    /**
     * Return all children of this element that are of the given type, and satisfy the given predicate.
     * 
     * @param predicate A predicate that determines which ancestors should be returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> children().contains(e) && predicate.eval(e));
     @*/
    public <T extends Element, E extends Exception> List<T> children(UniversalPredicate<T,E> predicate) throws E;
    
    /**
     * Return all children of this element that are of the given type, and that have the given property.
     * 
     * @param c The kind of the children that are returned.
     * @param property A property that must be satisfied by the children that are returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> children().contains(e) && c.isInstance(e) && e.isTrue(property));
     @*/
    public <T extends Element> List<T> children(Class<T> c, final ChameleonProperty property);
    
    /**
     * Recursively return all children of this element.
     * (The children, and the children of the children,...).
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Element e; \result.contains(e) <==> children().contains(e) ||
     @                                                   (\exists Element c; children().contains(c); c.descendants().contains(e)));
     @*/ 
    public List<Element> descendants();

    /**
     * Recursively return all descendants of this element that are of the given type.
     * 
     * @param c The type of descendants that should be returned.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> descendants().contains(e) && c.isInstance(e));
     @*/
    public <T extends Element> List<T> descendants(Class<T> c);
    
    
    /**
     * Check whether the given element is an ancestor of this element.
     * 
     * @param ancestor The potential ancestor.
     */
   /*@
     @ public behavior
     @
     @ pre c != null;
     @
     @ post \result == ancestors().contains(ancestor);
     @*/
	  public boolean hasAncestor(Element ancestor);


    /**
     * Check whether this element has a descendant of the given type.
     * 
     * @param c The class object representing the type the descendants.
     */
   /*@
     @ public behavior
     @
     @ pre c != null;
     @
     @ post \result == ! descendants(c).isEmpty();
     @*/
	  public <T extends Element> boolean hasDescendant(Class<T> c);

	  public default <T extends Element, E extends Exception> boolean hasDescendant(Class<T> type, Predicate<T,E> predicate) throws E {
	    List<T> result = children(type, predicate);
	    if (!result.isEmpty())
	      return true;

	    for (Element e : children()) {
	      if (e.hasDescendant(type, predicate))
	        return true;
	    }

	    return false;
	  }
	  /**
	   * Check whether this element has a descendant that satisfies the given predicate.
	   * 
	   * @param predicate The predicate of which must be determined whether any descendants satisfy it.
	   */
	 /*@
	   @ public behavior
	   @
	   @ pre predicate != null;
	   @
	   @ post \result == (\exists T t; descendants().contains(t); predicate.eval(t));
	  */
	  public <T extends Element, E extends Exception> boolean hasDescendant(UniversalPredicate<T,E> predicate) throws E;
  
	  
    /**
     * Return the descendants of the given type that are themselves no descendants of an element of the given type. In other words,
     * do a deep search for elements of the given type, but if you have found one, don't search its descendants.
     *
     * @param <T> The type of the elements for which the predicate can match.
     * @param c The type of the descendants that should be returned.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public <T extends Element> List<T> nearestDescendants(Class<T> c);

    /**
     * Return the list of first descendants that satisfy the given predicate. First means that if
     * an element satisfies the predicate, the element itself is in the result but its descendants are ignored.
     * 
     * @param <T> The type of the elements for which the predicate can match.
     * @param predicate The predicate that must be satisfied.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public <T extends Element, E extends Exception> List<T> nearestDescendants(UniversalPredicate<T,E> predicate) throws E;

    /**
     * Recursively return all descendants of this element that are of the given type, and have the given property.
     * 
     * @param c The kind of the children that are returned.
     * @param property A property that must be satisfied by the children that are returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> descendants().contains(e) && c.isInstance(e) && e.isTrue(property));
     @*/
    public <T extends Element> List<T> descendants(Class<T> c, ChameleonProperty property);

   /**
     * Recursively return all descendants of this element that satisfy the given predicate.
     * 
     * @param predicate A predicate that determines which descendants are returned.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> descendants().contains(e) && predicate.eval(e));
     @*/
    public <E extends Exception> List<Element> descendants(Predicate<? super Element,E> predicate) throws E;

    
    /**
     * Recursively return all descendants of this element that are of the given type, and satisfy the given predicate.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \result != null;
     @ post (\forall Element e; ; \result.contains(e) <==> descendants().contains(e) && c.isInstance(e) && predicate.eval(e));
     @*/
    public <T extends Element, E extends Exception> List<T> descendants(Class<T> c, Predicate<T,E> predicate) throws E;

     
    /**
     * Recursively apply the given action to this element and all of its 
     * descendants, but only if their type conforms to T.
     * 
     * @param action The action to apply.
     */
    public <T extends Element, E extends Exception> void apply(Action<T,E> action) throws E;
    
    
    /**
     * Recursively pass this element and all of its descendants to
     * the given consumer if their type conforms to T.
     * 
     * @param consumer The consumer to which the elements must be provided.
     * @param kind A class object representing the type of elements to be 
     *             passed to the consumer.
     */
    public <T extends Element, E extends Exception> void apply(Consumer<T> consumer, Class<T> kind) throws E;

    /**
     * Return the metadata with the given key.
     * @param key
     *        The key under which the metadata is registered.
     */
   /*@
     @
     @*/
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
     * Check whether or not this element has metadata.
     */
   /*@
     @ public behavior
     @
     @ post \result == ! metadata().isEmpty();
     @*/
    public boolean hasMetadata();
    
    /**
     * Return the farthest ancestor.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == this;
     @ post parent() != null ==> \result == parent().furthestAncestor();
     @*/
    public Element farthestAncestor();
    
    /**
     * Return the farthest ancestor of the given type.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == null;
     @ post parent() != null && c.isInstance(parent()) && parent().farthestAncestor(c) == null ==> \result == parent();
     @ post parent() != null && (parent().farthestAncestor(c) != null) ==> \result == parent().farthestAncestor(c);
     @*/
    public <T extends Element> T farthestAncestor(Class<T> c);
    
    /**
     * Return the farthest ancestor of the given type.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == null;
     @ post parent() != null && c.isInstance(this) && parent().farthestAncestor(c) == null ==> \result == this;
     @ post parent() != null && (parent().farthestAncestor(c) != null) ==> \result == parent().farthestAncestor(c);
     @*/
    public <T extends Element> T farthestAncestorOrSelf(Class<T> c);
    
    /**
     * Return the farthest ancestor of the given type that satisfies the given predicate.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == null;
     @ post parent() != null && c.isInstance(this) && parent().farthestAncestor(c) == null ==> \result == this;
     @ post parent() != null && (parent().farthestAncestor(c) != null) ==> \result == parent().farthestAncestor(c);
     @*/
    public <T extends Element, E extends Exception> T farthestAncestor(UniversalPredicate<T,E> p) throws E;

    /**
     * Return the nearest ancestor of type T. Null if no such ancestor can be found.
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == null;
     @ post parent() != null && c.isInstance(parent()) ==> \result == parent();
     @ post parent() != null && (! c.isInstance(parent())) ==> \result == parent().nearestAncestor(c);
     @*/
    public <T extends Element> T nearestAncestor(Class<T> c);
    
    /**
     * Return the nearest ancestor of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result == null;
     @ post parent() != null && c.isInstance(parent()) && predicate.eval((T)parent()) ==> \result == parent();
     @ post parent() != null && ((! c.isInstance(parent())) || (c.isInstance(parent()) && ! predicate.eval((T)parent())) 
     @          ==> \result == parent().nearestAncestor(c, predicate);
     @*/
    public <T extends Element, E extends Exception> T nearestAncestor(UniversalPredicate<T,E> predicate) throws E;

    /**
     * Return the nearest element of type T. Null if no such ancestor can be found.
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
   /*@
     @ public behavior
     @
     @ post c.isInstance(this) ==> \result == this;
     @ post ! c.isInstance(this) && parent() != null ==> \result == parent().nearestAncestor(c);
     @ post ! c.isInstance(this) && parent() == null ==> \result == null;
     @*/
    public <T extends Element> T nearestAncestorOrSelf(Class<T> c);
    
    /**
     * Return the nearest element of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
   /*@
     @ public behavior
     @
     @ post c.isInstance(this) && predicate.eval(this) ==> \result == this;
     @ post (! predicate.eval(this)) && parent() != null ==> \result == parent().nearestAncestor(predicate);
     @ post (! predicate.eval(this)) && parent() == null ==> \result == null;
     @*/
    public <T extends Element, E extends Exception> T nearestAncestorOrSelf(UniversalPredicate<T, E> predicate) throws E;
    
    /**
     * Return the language of this element. Return null if this element is not
     * connected to a complete model.
     */
   /*@
     @ public behavior
     @
     @ post (parent() == null) ==> (\result == null);
     @*/
    public Language language();
    
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
    public Project project();
    
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
    public <T extends View> T view(Class<T> kind) throws WrongViewException;
    
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
    public default <T extends Element> T clone(T element) {
      // Without the cast, the clone call is bound to the
      // clone of Object, which throws a CloneNotSupportedException.
      // Is this because of a potential Raw method call? If you write
      // the same code in ElementImpl, the cast is not required.
        return (T) ((Element)element).clone();
    }
    
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
     * Create a undirectional parent association to the given element. If the given parent is not null,
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
     * Return the default properties of this element. A default property
     * is a property that an element has when no inherent or explicitly
     * defined property contradicts that property.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public PropertySet<Element,ChameleonProperty> defaultProperties();
    
    /**
     * Return a property set representing the properties of this element
     * that are explicitly declared.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public PropertySet<Element,ChameleonProperty> declaredProperties();


    /**
     * Return a the properties that are inherent to this element.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public PropertySet<Element,ChameleonProperty> inherentProperties();
    
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
     * Return the property of this element for the given property mutex. The property mutex
     * can be seen as the family of properties that a property belongs to. An example of a
     * property mutex is accessibility.
     * 
     * @param mutex
     * @return
     * @throws LookupException 
     */
   /*@
     @ public behavior
     @
     @ pre mutex != null;
     @
     @ post properties().contains(\result);
     @ post \result.mutex() == mutex;
     @ post (\num_of Property p; properties().contains(p);
     @       p.mutex() == mutex) == 1;
     @
     @ signals ModelException (\num_of Property p; properties().contains(p);
     @       p.mutex() == mutex) != 1; 
     @*/
    public ChameleonProperty property(PropertyMutex<ChameleonProperty> mutex) throws ModelException;
    
    /**
     * Return the single property that implies the given property. If more than one property of this
     * element implies the given property, an exception is thrown.
     * 
     * @param implied
     * @return
     * @throws ModelException
     */
   /*@
     @ public behavior
     @
     @ pre implied != null;
     @
     @ post properties().contains(\result);
     @ post \result.implies(implied);
     @ post (\num_of Property p; properties().contains(p);
     @       p.implies(implied)) == 1;
     @
     @ signals ModelException (\num_of Property p; properties().contains(p);
     @       p.implies(implied)) != 1; 
     @*/
//    public ChameleonProperty implyingProperty(final ChameleonProperty implied) throws ModelException;
    
    /**
     * Check whether this element has a property from the given property mutex.
     * 
     * @param mutex
     * @return
     * @throws ModelException
     */
   /*@
     @ public behavior
     @
     @ pre mutex != null;
     @
     @ post \result properties().hasPropertyFor(mutex);
     @*/
    public boolean hasProperty(PropertyMutex<ChameleonProperty> mutex) throws ModelException;
    
    /**
     * Notify this element that the given descendant was modified. This
     * method first calls reactOnDescendantChange with the given element. After that,
     * the event is propagated to the lexical parent, if the parent is not null.
     */
   /*@
     @ public behavior
     @
     @ pre descendant != null;
     @ pre descendants().contains(descendant);
     @*/
    public void notifyDescendantChanged(Element descendant);
    
    
    /**
     * Notify this element that the given descendant was modified. This method
     * only performs the local reaction to the event.
     */
    public void reactOnDescendantChange(Element descendant);
    
    /**
     * Notify this element that the given descendant was added. This method
     * only performs the local reaction to the event.
     */
    public void reactOnDescendantAdded(Element descendant);
    
    /**
     * Notify this element that the given descendant was removed. This method
     * only performs the local reaction to the event.
     */
    public void reactOnDescendantRemoved(Element descendant);
    
    /**
     * Notify this element that the given descendant was replaced. This method
     * only performs the local reaction to the event.
     */
    public void reactOnDescendantReplaced(Element oldElement, Element newElement);

    /**
     * Verify whether or not this is valid, and if not, what the problems are. The verification looks recursively
     * for all problems.
     * @return
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
     * Because equals cannot throw checked exceptions, we have some framework code for equality.
     * 
     * First of all: DO NOT USE EQUALS!!! Use sameAs(Element) instead.
     * 
     * To ensure that equals work correctly when it is invoked by third-party code (e.g. java.util.HashSet),
     * equals is implemented in terms of sameAs, which in turn is implemented as uniSameAs. The latter method checks
     * if an object states that it is equal to another (similar to equals). The sameAs method invokes this method on both
     * objects to check if one of both objects claims to be the same as the other.
     */
   /*@
     @ public behavior
     @
     @ post (other instanceof Element) && sameAs((Element)other);
     @
     @ signals (RuntimeMetamodelException) (* sameAs(other) has thrown an exception *);
     @*/
    @Override 
    public abstract boolean equals(Object other);
    
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
    public abstract boolean sameAs(Element other) throws LookupException;

    /**
     * Check whether this element is the same as the other element.
     */
   /*@
     @ public behavior
     @
     @ post other == null ==> \result == false;
     @*/
    public abstract boolean uniSameAs(Element other) throws LookupException;
    
    /**
     * Return the namespace of this element.
     * 
     * By default, it returns the namespace of the nearest namespace declaration.
     * @return
     */
  	public Namespace namespace();

    /**
     * Flush any caching this element may have.
     * This method flushes the local cache using "flushLocalCache()" and then
     * recurses into the children.
     */
    public abstract void flushCache();
    
    public EventManager when();
}
