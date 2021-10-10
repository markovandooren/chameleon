package org.aikodi.chameleon.eclipse.view.dependency;

import org.aikodi.chameleon.analysis.dependency.DependencyResult;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.rejuse.data.graph.Node;
import org.aikodi.rejuse.predicate.UniversalPredicate;
import org.eclipse.gef.zest.fx.jface.IGraphContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyContentProvider implements IGraphContentProvider {

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
  public Object[] getAdjacentNodes(Object input) {
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
  public Object[] getNestedGraphNodes(Object object) {
    if(NESTED) {
      Node<Element> node = (Node<Element>) object;
      List<Element> list = new ArrayList<>();
      Set<Element> all = _result.elements();
      List<Node<Element>> result = node.object().lexical().nearestDescendants(UniversalPredicate.of(Element.class, e -> all.contains(e))).stream().map(e -> _result.nodeOf(e)).collect(Collectors.toList());
      return result.toArray();
    } else {
      return null;
    }
  }

  @Override
  public boolean hasNestedGraph(Object node) {
    return NESTED && getNestedGraphNodes(node).length > 0;
  }

}
