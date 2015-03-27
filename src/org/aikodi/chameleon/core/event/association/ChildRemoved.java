package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event to signal that a child was removed.
 * 
 * @author Marko van Dooren
 */
public class ChildRemoved extends AssociationChanged implements Removed, ChildChanged {

  /**
   * Create a new event to signal that the given child was added.
   * 
   * @param child The child that was added.
   */
  public ChildRemoved(Element child) {
    super(child,null);
  }

  @Override
  public Element element() {
    return removedElement();
  }

}
