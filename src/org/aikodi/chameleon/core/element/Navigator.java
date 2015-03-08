package org.aikodi.chameleon.core.element;

import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

public abstract class Navigator extends TreeStructure<Element> {

  /**
   * {@inheritDoc}
   * 
   * @return the parent of the given element.
   */
  @Override
  public Element parent() {
    return ((ElementImpl) node()).actualParent();
  }

  /**
   * {@inheritDoc}
   * 
   * @return The {@link Element#children()} of the element.
   */
  @Override
  public List<? extends Element> children() {
    return node().children();
  }
}