package chameleon.core.element;

import java.util.Collection;
import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.Context;
import chameleon.core.context.LexicalContext;
import chameleon.core.language.Language;
import chameleon.core.tag.Tag;
import chameleon.linkage.ILinkage;
import chameleon.linkage.IMetaModelFactory;

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
 * @author Marko van Dooren
 * @author Koen Vanderkimpen
 */

public interface Element<E extends Element, P extends Element> {

	  /**
	   * Return the parent element of this element. Null if there is no parent.
	   */
    public P parent();
    
    /**
     * Return the object representing the <b>bidirectional</b>link to the parent of this element.
     * 
     * This link is <b>NOT</b> used for elements that are generated! Always use parent() to obtain
     * the parent.
     */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
    public Reference<E,P> parentLink();

    /**
     * Completely disconnect this element and all children from the parent.
     */
   /*@
     @ public behavior
     @
     @ post parent() == null;
     @*/
    public void disconnect();
    
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
     * Recursively return all children of this element.
     */
   /*@
     @ public behavior
     @
     @ post \result == descendants(Element.class);
     @*/ 
    public List<Element> descendants();

    /**
     * Recursively return all descendants of this element.
     * (The children, and the children of the children,...).
     */
   /*@
     @ public behavior
     @
     @ post \forall(Element e; children().contains(c) && c.isInstance(e); \result.contains(e));
     @ post \forall(Element e; children().contains(c); \result.containsAll(e.descendants()));
     @*/
    public <T extends Element> List<T> descendants(Class<T> c);

    /**
     * Return the tag with the given name.
     * @param name
     *        The name under which the tag is registered.
     */
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
     * Check whether or not this element has tags.
     */
   /*@
     @ public behavior
     @
     @ post \result == ! tags().isEmpty();
     @*/
    public boolean hasTags();
    
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
     * @throws MetamodelException 
     */
   /*@
     @ public behavior
     @
     @ pre children().contains(child); 
     @*/
    public Context lexicalContext(Element child) throws MetamodelException;
    
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
    public Context lexicalContext() throws MetamodelException;
    
    /**
     * DO NOT USE THIS METHOD UNLESS YOU REALLY KNOW WHAT YOU ARE DOING!!!
     * 
     * This method is used internally for generated elements. They are not part
     * of the model, but need a parent in order to have a context.
     * 
     * @param parent
     */
    public void setUniParent(P parent);
}
