package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

public abstract class AssociationChanged {

  private Element _removedElement;
  
  private Element _addedElement;

  /**
   * Create a new event to indicate that the given old element was
   * removed and the given new element was added.
   * 
   * @param removedElement The element that was removed.
   *                   The element cannot be null.
   * @param addedElement The element that was added.
   *                   The element cannot be null.
   */
  public AssociationChanged(Element removedElement, Element addedElement) {
    this._removedElement = removedElement;
    this._addedElement = addedElement;
  }
  
  /**
   * @return The element that was removed.
   */
  public Element removedElement() {
    return _removedElement;
  }
  
  /**
   * @return The replacement element.
   */
  public Element addedElement() {
    return _addedElement;
  }

}
