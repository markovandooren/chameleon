package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event to signal that a parent was removed.
 * 
 * @author Marko van Dooren
 */
public class ParentRemoved extends AssociationChanged implements Removed, ParentChanged {

  /**
   * Create a new event to signal that the given parent was added.
   * 
   * @param parent The parent that was added.
   */
  public ParentRemoved(Element parent) {
    super(parent, null);
  }

  @Override
  public Element element() {
    return removedElement();
  }

}
