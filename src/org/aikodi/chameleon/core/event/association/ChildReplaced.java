package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.contract.Contracts;

/**
 * An event that indicates that a child was replaced by another child.
 * 
 * @author Marko van Dooren
 */
public class ChildReplaced extends AssociationChanged implements Replaced, ChildChanged {

  /**
   * Create a new event to indicate that the given old element was
   * replaced by the given new element.
   * 
   * @param oldChild The child that was replaced.
   *                   The element cannot be null.
   * @param newChild The replacement child.
   *                   The element cannot be null.
   */
  public ChildReplaced(Element oldChild, Element newChild) {
    super(oldChild,newChild);
    Contracts.notNull(oldChild, "The replaced element cannot be null.");
    Contracts.notNull(newChild, "The replacement element cannot be null.");
  }
  
}
