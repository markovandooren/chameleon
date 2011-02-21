package chameleon.core.element;

import java.util.Collection;
import java.util.List;

import org.rejuse.association.SingleAssociation;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.Predicate;
import org.rejuse.predicate.SafePredicate;
import org.rejuse.predicate.UnsafePredicate;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;

import chameleon.core.language.Language;
import chameleon.core.language.WrongLanguageException;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.tag.Tag;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ModelException;

/**
 * Element is the top interface for an element of a model.
 * 
 * Every element can have a parent and children.
 * 
 * Every element can have tags associated with it. They are used to attach additional information
 * to an element without modifying Chameleon.
 * 
 * <E> The type of the element (typically the subclass being defined).
 * <P> The type of the parent of the element.
 * 
 * As the client of a model, you can mostly ignore these parameters. Because Java
 * supports parametric polymorphism only through functional-style generic parameters,
 * we cannot hide them. They are almost exclusively there for internal purposes. With
 * type members, these problems would not occur.
 * 
 * @author Marko van Dooren
 * 
 * @opt all
 */
public interface Element<E extends Element> {

	  /**
	   * Return the parent element of this element. Null if there is no parent.
	   */
    public Element parent();
    
    /**
     * Return the object representing the <b>bidirectional</b>link to the parent of this element.
     * 
     * This link is <b>NOT</b> used for elements that are generated! Always use parent() to obtain
     * the parent. This method is only public due to the limitations of Java.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public SingleAssociation<E,Element> parentLink();

    /**
     * Completely disconnect this element and all children from the parent.
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
	   * Check if this element is derived or not.
	   * 
	   * @return True if this element is derived, false otherwise.
	   */
	  public boolean isDerived();
	  
	  /**
	   * If this element is derived, return the element from which this element originates.
	   * If this element is not derived, the method returns the current object.
	   */
	 /*@
	   @ public behavior
	   @
	   @ post ! isDerived() ==> \result == this;
	   @*/
	  public Element origin();
	  
	  public void setOrigin(Element element);
	  

	  /**
     * Return a list of all ancestors. The direct parent is in front of the list, the
     * furthest ancestor is last.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null ==> \result.get(0) == parent();
     @ post parent() != null ==> \result.subList(1,\result.size()).equals(parent().ancestors());
     @*/
    public List<Element> ancestors();

    /**
     * Return a list of all ancestors of the given type. A closer ancestors will have a lower index than a 
     * farther ancestor.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null && c.isInstance(parent()) ==> \result.get(0) == parent();
     @ post parent() != null ==> \result.subList(1,\result.size()).equals(parent().ancestors(c));
     @*/
    public <T extends Element<?>> List<T> ancestors(Class<T> c);
    
    /**
     * Return a list of all ancestors of the given type that satify the given predicate. 
     * A closer ancestors will have a lower index than a farther ancestor.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null ==> \result.isEmpty();
     @ post parent() != null && c.isInstance(parent()) && predicate.eval(parent()) ==> \result.get(0) == parent();
     @ post parent() != null ==> \result.subList(1,\result.size()).equals(parent().ancestors(c));
     @*/
    public <T extends Element<?>> List<T> ancestors(Class<T> c, SafePredicate<T> predicate);

    
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
     @ post \forall(Element e; \result.contains(e); e.parent() == this);
     @*/
    public List<? extends Element> children();

    
    /**
     * Return all children of this element that are of the given type.
     */
   /*@
     @ public behavior
     @
     @ post \forall(Element e; children().contains(e) && c.isInstance(e); \result.contains(e));
     @*/
    public <T extends Element> List<T> children(Class<T> c);
    
    /**
     * Return all children of this element that satisfy the given predicate.
     * 
     * The only checked exceptions that can occur will come from the predicate. Use the safe and unsafe variants
     * of this method for convenience.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public List<Element> children(Predicate<Element> predicate) throws Exception;
    
    /**
     * Return all children of this element that satisfy the given predicate.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public List<Element> children(SafePredicate<Element> predicate);

    /**
     * Return all children of this element that satisfy the given predicate.
     * 
     * The only checked exceptions that can occurs are determined by type parameter X.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public <X extends Exception> List<Element> children(UnsafePredicate<Element,X> predicate) throws X;
    
    /**
     * Return all children of this element that are of the given type, and satisfy the given predicate.
     * 
     * The only checked exception that occurs will come from the predicate. Use the safe and unsafe variants
     * of this method for convenience.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public <T extends Element> List<T> children(Class<T> c, Predicate<T> predicate) throws Exception;
    
    /**
     * Return all children of this element that are of the given type, and satisfy the given predicate.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public <T extends Element> List<T> children(Class<T> c, SafePredicate<T> predicate);
    
    /**
     * Return all children of this element that are of the given type, and satisfy the given predicate.
     * 
     * The only checked exception that occurs are determined by type parameter X.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; children().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
    public <T extends Element, X extends Exception> List<T> children(Class<T> c, UnsafePredicate<T,X> predicate) throws X;
    
    /**
     * Recursively return all children of this element.
     * (The children, and the children of the children,...).
     */
   /*@
     @ public behavior
     @
     @ post \forall(Element e; children().contains(c); \result.contains(e) && \result.containsAll(e.descendants()));
     @*/ 
    public List<Element> descendants();

    /**
     * Recursively return all descendants of this element that are of the given type.
     */
   /*@
     @ public behavior
     @
     @ post \forall(Element e; descendants().contains(e) && c.isInstance(e); \result.contains(e));
     @*/
    public <T extends Element> List<T> descendants(Class<T> c);

    
    /**
     * Recursively return all descendants of this element that satisfy the given predicate.
     * 
     * The only checked exceptions that can occur will come from the predicate. Use the safe and unsafe variants
     * of this method for convenience.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; descendants().contains(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
   public List<Element> descendants(Predicate<Element> predicate) throws Exception;

    /**
     * Recursively return all descendants of this element that satisfy the given predicate.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; descendants().contains(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
   public List<Element> descendants(SafePredicate<Element> predicate);
   
   /**
    * Recursively return all descendants of this element that satisfy the given predicate.
    * 
    * The only checked exceptions that can occurs are determined by type parameter X.
    */
  /*@
    @ public behavior
    @
    @ pre predicate != null;
    @
    @ post \forall(Element e; descendants().contains(e) && predicate.eval(e) == true; \result.contains(e));
    @*/
   public <X extends Exception> List<Element> descendants(UnsafePredicate<Element,X> predicate) throws X;
   
    /**
     * Recursively return all descendants of this element that are of the given type, and satisfy the given predicate.
     * 
     * The only checked exception that occurs will come from the predicate. Use the safe and unsafe variants
     * of this method for convenience.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; descendants().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
   public <T extends Element> List<T> descendants(Class<T> c, Predicate<T> predicate) throws Exception;

    /**
     * Recursively return all descendants of this element that are of the given type, and satisfy the given predicate.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; descendants().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
   public <T extends Element> List<T> descendants(Class<T> c, SafePredicate<T> predicate);

    /**
     * Recursively return all descendants of this element that are of the given type, and satisfy the given predicate.
     * 
     * The only checked exception that occurs are determined by type parameter X.
     */
   /*@
     @ public behavior
     @
     @ pre predicate != null;
     @
     @ post \forall(Element e; descendants().contains(e) && c.isInstance(e) && predicate.eval(e) == true; \result.contains(e));
     @*/
   public <T extends Element, X extends Exception> List<T> descendants(Class<T> c, UnsafePredicate<T,X> predicate) throws X;

    /**
     * Return the tag with the given name.
     * @param name
     *        The name under which the tag is registered.
     */
   /*@
     @
     @*/
    public Tag tag(String name);

    /**
     * Return all tags associated with this element.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post (\forall Tag tag;; 
     @        \result.contains(tag) == (\exists String s;; tag(s) == tag));
     @*/
    public Collection<Tag> tags();
    
    /**
     * Check whether or not a tag is registered under the given name
     * @param name
     *        The name to be checked
     * @return
     */
   /*@
     @ public behavior
     @
     @ pre name != null;
     @
     @ post \result == (tag(name) != null);
     @*/
    public boolean hasTag(String name);

    /**
     * Register the given tag under the given name. 
     * 
     * If an element was already
     * registered under the given name, that element will be unregistered and its
     * element reference set to null such that the bidirectional association is kept consistent.
     * 
     * @param tag
     *        The tag to be registered
     * @param name
     *        The name under which the given tag must be registered,
     */
   /*@
     @ public behavior
     @
     @ pre name != null;
     @ pre tag != null;
     @
     @ post tag(name) == tag;
     @*/
    public void setTag(Tag tag, String name);

    /**
     * Remove the tag registered under the given name.
     * @param name
     *        The name of the tag to be removed.
     */
   /*@
     @ public behavior
     @
     @ post tag(name) == null;
     @*/
    public void removeTag(String name);
    
    /**
     * Remove all tags from this element
     */
   /*@
     @ public behavior
     @
     @ post tags().isEmpty();
     @*/
	  public void removeAllTags();

    /**
     * Check whether or not this element has tags.
     */
   /*@
     @ public behavior
     @
     @ post \result == ! tags().isEmpty();
     @*/
    public boolean hasTags();
    
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
     * Return the nearest ancestor of type T. Null if no such ancestor can be found.
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestAncestor(Class<T> c);
    
    /**
     * Return the nearest ancestor of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestAncestor(Class<T> c, Predicate<T> predicate) throws Exception;

    /**
     * Return the nearest ancestor of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestAncestor(Class<T> c, SafePredicate<T> predicate);
    
    /**
     * Return the nearest ancestor of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element, X extends Exception> T nearestAncestor(Class<T> c, UnsafePredicate<T,X> predicate) throws X;
    
    /**
     * Return the nearest element of type T. Null if no such ancestor can be found.
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestElement(Class<T> c);
    
    /**
     * Return the nearest element of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestElement(Class<T> c, Predicate<T> predicate) throws Exception;

    /**
     * Return the nearest element of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T nearestElement(Class<T> c, SafePredicate<T> predicate);
    
    /**
     * Return the nearest element of type T that satifies the given predicate. Null if no such ancestor can be found.
     * 
     * The only checked exception that can be thrown comes from the predicate. Use the safe and unsafe variants of this method
     * for convenience.
     * 
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element, X extends Exception> T nearestElement(Class<T> c, UnsafePredicate<T,X> predicate) throws X;
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
     * Return a deep clone of this element. The returned element has no parent. 
     * @return
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @ post \result.getParent() == null;
     @*/
    public E clone();
    
    /**
     * Return the lexical context for the given child element. The
     * default behavior is to delegate the search to the parent.
     * 
     * If this element declares elements itself, it searches those
     * elements first, and only delegates to the parent if nothing
     * is found.
     * 
     * @param element
     *        The child element of this element for which the
     *        context is requested
     * @throws LookupException 
     */
   /*@
     @ public behavior
     @
     @ pre children().contains(child); 
     @*/
    public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException;
    
    /**
     * Return the lexical context for this element.
     */
   /*@
     @ public behavior
     @
     @ post \result == parent().lexicalContext(this);
     @
     @ signals (MetamodelException) parent() == null; 
     @*/
    public LookupStrategy lexicalLookupStrategy() throws LookupException;
    
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
     * @param parent
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
     * Return the default properties of this element.
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
     @ post \result != null ==> properties().contains(\result);
     @ post \result != null ==> \result.mutex() == mutex;
     @ post (\num_of Property p; properties().contains(p);
     @       p.mutex() == mutex) == 1 ==> \result != null;
     @
     @ signals MetamodelException (\num_of Property p; properties().contains(p);
     @       p.mutex() == mutex) > 1; 
     @*/
    public ChameleonProperty property(PropertyMutex<ChameleonProperty> mutex) throws ModelException;
    
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
    public VerificationResult verify();

    /*************************
     * IDENTITY and EQUALITY *
     *************************/
    
    /**
     * Because equals cannot throw MetamodelExceptions, we have some framework code for equality.
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
     @ signals (RuntimeMetamodelException)
     @*/
    @Override 
    public abstract boolean equals(Object other);
    
    /**
     * Check if this element is the same as the other element.
     */
   /*@
     @ public behavior
     @
     @ post other == null ==> \result == uniSameAs(other);
     @ post other != null ==> \result == uniSameAs(other) || other.uniSameAs(this);
     @*/
    public abstract boolean sameAs(Element other) throws LookupException;

    /**
     * Check whether this element is the same as the other element.
     */
    public abstract boolean uniSameAs(Element other) throws LookupException;
    
    /**
     * Flush any caching this element may have.
     * This method flushes the local cache using "flushLocalCache()" and then
     * recurses into the children.
     */
    public abstract void flushCache();
}
