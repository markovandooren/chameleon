package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event to signal that a child was added.
 * 
 * @author Marko van Dooren
 */
public class ChildAdded extends AssociationChanged implements Added, ChildChanged {

  /**
   * Create a new event to signal that the given child was added.
   * 
   * @param child The child that was added.
   */
  public ChildAdded(Element child) {
    super(null,child);
  }

  @Override
  public Element element() {
    return addedElement();
  }

}
