package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event that indicates that an element was replaced by another element.
 * 
 * @author Marko van Dooren
 */
public class ParentReplaced extends AssociationChanged implements Replaced, ParentChanged {

  /**
   * Create a new event to indicate that the given old parent was
   * replaced by the given new parent.
   * 
   * @param oldParent The parent that was replaced.
   *                   The element cannot be null.
   * @param newParent The replacement parent.
   *                   The element cannot be null.
   */
  public ParentReplaced(Element oldParent, Element newParent) {
    super(oldParent, newParent);
  }

}
