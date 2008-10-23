/*
 * Copyright 2000-2004 the Chameleon development team.
 *
 * @author Marko van Dooren
 * @author Koen Vanderkimpen
 *
 * This file is part of Chameleon.
 *
 * Chameleon is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Chameleon is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Chameleon; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.element;

import java.util.Collection;
import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
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
    public P getParent();
    
    public Reference<E,P> getParentLink();

    /**
     * Completely disconnect this element and all children from the parent.
     */
    public void disconnect();
    
    /**
     * Return a list of all parents. The direct parent is in front of the list, the
     * furthest ancestor is last.
     */
   /*@
     @ public behavior
     @
     @ post getParent() == null ==> \result.isEmpty();
     @ post getParent() != null ==> \result.get(0) == getParent();
     @ post getParent() != null ==> \result.subList(1,\result.size()).equals(getParent().getAllParents());
     @*/
    public List<Element> getAllParents();

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
     @ post \forall(Element e; \result.contains(e); e.getParent() == this);
     @*/
    public List<? extends Element> getChildren();

    
    /**
     * Recursively return all children of this element.
     */
   /*@
     @ public behavior
     @
     @ post \result == getDescendants(Element.class);
     @*/ 
    public List<Element> getDescendants();

    /**
     * Recursively return all descendants of this element.
     * (The children, and the children of the children,...).
     */
   /*@
     @ public behavior
     @
     @ post \forall(Element e; getChildren().contains(c) && c.isInstance(e); \result.contains(e));
     @ post \forall(Element e; getChildren().contains(c); \result.containsAll(e.getDescendants()));
     @*/
    public <T extends Element> List<T> getDescendants(Class<T> c);

    /**
     * Return the tag with the given name.
     * @param name
     *        The name under which the tag is registered.
     */
    public Tag getDecorator(String name);

    /**
     * Return all decorators associated with this elements.
     */
    public Collection<Tag> getDecorators();

    /**
     * Check whether or not a tag is registered under the given name
     * @param name
     *        The name to be checked
     * @return
     */
    public boolean hasDecorator(String name);

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
    public void setDecorator(Tag tag, String name);

    /**
     * Remove the tag registered under the given name.
     * @param name
     *        The name of the tag to be removed.
     */
    public void removeTag(String name);
    
    /**
     * Check whether or not this element has tags.
     * @return
     */
    public boolean hasDecorators();
    
    /**
     * @return The parent compilation unit for this element, null if no parent
     *         is available
     * @deprecated        
     */
    public void reParse(ILinkage linkage, IMetaModelFactory factory);

    /**
     * Return the nearest ancestor of type T. Null if no such ancestor can be found.
     * @param <T>
     *        The type of the ancestor to be found
     * @param c
     *        The class object of type T (T.class)
     * @return
     */
    public <T extends Element> T getNearestAncestor(Class<T> c);
    
    /**
     * Return the language of this element. Return null if this element is not
     * connected to a complete model.
     */
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
     * Return the lexical context for the given child element.
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
    public LexicalContext lexicalContext(Element child) throws MetamodelException;
    
    public LexicalContext lexicalContext() throws MetamodelException;
}
