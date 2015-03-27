package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event to signal that a parent was added.
 * 
 * @author Marko van Dooren
 */
public class ParentAdded extends AssociationChanged implements Added, ParentChanged {

  /**
   * Create a new event to signal that the given parent was added.
   * 
   * @param child The child that was added.
   */
  public ParentAdded(Element parent) {
    super(null, parent);
  }

  @Override
  public Element element() {
    return addedElement();
  }

}
