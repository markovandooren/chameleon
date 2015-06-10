package org.aikodi.chameleon.eclipse.view.dependency;

import java.util.Set;
import java.util.stream.Collectors;

import org.aikodi.chameleon.analysis.dependency.DependencyResult;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.Util;
import org.eclipse.gef4.zest.fx.ui.jface.IGraphNodeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DependencyContentProvider implements IGraphNodeContentProvider {

  @Override
  public void dispose() {
    _result = null;
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    _result = (DependencyResult) newInput;
  }

  private DependencyResult _result;

  //	@Override
  //	public Node getSource(Object rel) {
  //		return ((UniEdge)rel).startNode();
  //	}
  //
  //	@Override
  //	public Node getDestination(Object rel) {
  //		return ((UniEdge)rel).endNode();
  //	}

  @Override
  public Object[] getConnectedTo(Object input) {
    Object[] result;
    if(_result != null) {
      Set<Element> dependencies = _result.dependencies((Element) input);
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
      Set<Element> elements = _result.elements();
      result = new Object[elements.size()];
      int i=0;
      for(Element e: elements) {
        result[i] = e;
        i++;
      }
    }
    return result;
  }

}
