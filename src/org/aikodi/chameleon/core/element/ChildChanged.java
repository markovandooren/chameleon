package org.aikodi.chameleon.core.element;

public abstract class ChildChanged extends Change {

  private Element _element;

  public ChildChanged(Element element) {
    _element = element;
  }

  public Element element() {
    return _element;
  }

}