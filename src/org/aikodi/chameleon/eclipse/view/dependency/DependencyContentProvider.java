package org.aikodi.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.aikodi.chameleon.analysis.dependency.DependencyResult;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.Util;
import org.eclipse.gef4.zest.fx.ui.jface.INestedGraphContentProvider;
import org.eclipse.jface.viewers.Viewer;

import be.kuleuven.cs.distrinet.rejuse.graph.Node;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class DependencyContentProvider implements INestedGraphContentProvider {

  private static boolean NESTED = false;

  @Override
  public void dispose() {
    _result = null;
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    _result = (DependencyResult) newInput;
  }

  private DependencyResult _result;

  @Override
  public Object[] getConnectedTo(Object input) {
    Object[] result;
    if(_result != null) {
      Set<Node<Element>> dependencies = ((Node<Element>) input).directSuccessorNodes();
      result = dependencies.stream().collect(Collectors.toList()).toArray();
    } else {
      result = new Object[0];
    }
    return result;
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public Object[] getNodes() {
    Object[] result;
    if(_result == null) {
      result = new Object[0];
    } else {
      if(NESTED) {
        Set<Element> all = _result.elements();
        Set<Element> roots = new HashSet<>();
        all.forEach(e -> {
          Element farthest = e.logical().farthestAncestorOrSelf(Element.class, d -> all.contains(d));
          roots.add(farthest);
        });
        result = roots.stream().map(e -> _result.nodeOf(e)).collect(Collectors.toList()).toArray();
      } else {
        Set<Node<Element>> elements = _result.nodes();
        result = elements.stream().collect(Collectors.toList()).toArray();
      }
    }
    return result;
  }

  @Override
  public Object[] getChildren(Object object) {
    if(NESTED) {
      Node<Element> node = (Node<Element>) object;
      List<Element> list = new ArrayList<>();
      Set<Element> all = _result.elements();
      List<Node<Element>> result = node.object().nearestDescendants(UniversalPredicate.of(Element.class, e -> all.contains(e))).stream().map(e -> _result.nodeOf(e)).collect(Collectors.toList());
      return result.toArray();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasChildren(Object node) {
    return NESTED && getChildren(node).length > 0;
  }

}
