package org.aikodi.chameleon.eclipse.view.dependency;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.analysis.dependency.DependencyResult.DependencyCount;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorExtension;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.rejuse.data.graph.Edge;
import org.aikodi.rejuse.data.graph.Node;
import org.aikodi.rejuse.data.graph.UniEdge;
import org.eclipse.gef.zest.fx.ZestProperties;
import org.eclipse.gef.zest.fx.jface.IGraphAttributesProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import javafx.scene.shape.Polygon;

class DependencyLabelProvider extends LabelProvider implements IGraphAttributesProvider,  IColorProvider {//implements IConnectionStyleProvider, IEntityStyleProvider {


  @Override
  public String getText(Object element) {
    if(element instanceof Node) {
      element = ((Node)element).object();
    }

    if(element instanceof Element) {
      return ((Element)element).language().plugin(EclipseEditorExtension.class).label((Element) element);
    } else if(element instanceof UniEdge) {
      DependencyCount count = ((UniEdge<Element>)element).get(DependencyCount.class);
      return ""+count.value();
    } else {
      return "";
    }
  }

  @Override
  public Image getImage(Object element) {
    if(element instanceof Node) {
      element = ((Node)element).object();
    }
    if(element instanceof Element) {
      try {
        return ((Element)element).language().plugin(EclipseEditorExtension.class).icon((Element) element);
      } catch(ModelException exc) {
        exc.printStackTrace();
      }
    }
    return super.getImage(element);
  }

  @Override
  public Map<String, Object> getNodeAttributes(Object node) {
    Map<String, Object> result = new HashMap<>();
    return result;
  }

	static class DiamondHead extends Polygon {
		public DiamondHead() {
			super(15.0, 0.0, 7.5, -7.5, 0.0, 0.0, 7.5, 7.5, 15.0, 0.0);
		}
	}

  @Override
  public Map<String, Object> getEdgeAttributes(Object sourceNode, Object targetNode) {
    Map<String, Object> result = new HashMap<>();
    Node<Element> start = (Node<Element>) sourceNode;
    Node<Element> end = (Node<Element>) targetNode;
    Set<Edge<Element>> outgoingEdges = start.outgoingEdges(end);
    if(outgoingEdges.size() > 0) {
      DependencyCount count = ((Edge<Element>)outgoingEdges.iterator().next()).get(DependencyCount.class);
      String label = ""+count.value();
      result.put("label",label);
    }
    result.put(ZestProperties.SOURCE_DECORATION__E, new DiamondHead());
    return result;
  }

  @Override
  public Map<String, Object> getGraphAttributes() {
    Map<String, Object> result = new HashMap<>();
//    result.put(GRAPH_TYPE, GRAPH_TYPE_DIRECTED);
    return result;
  }

  //  @Override
  //  public int getConnectionStyle(Object rel) {
  //    return ZestStyles.CONNECTIONS_SOLID;
  //  }
  //
  //  @Override
  //  public Color getColor(Object rel) {
  //    return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
  //  }
  //
  //  @Override
  //  public Color getHighlightColor(Object rel) {
  //    return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
  //  }
  //
  //  @Override
  //  public int getLineWidth(Object rel) {
  //    return 1;
  //  }
  //
  //  @Override
  //  public IFigure getTooltip(Object entity) {
  //    if(entity instanceof Node) {
  //      entity = ((Node)entity).object();
  //    }
  //    //FIXME: This is a temporary hack. This label provider should of course 
  //    // have no knowledge of types or variables.
  //    if(entity instanceof Type) {
  //      return new Label(((Type)entity).getFullyQualifiedName());
  //    } else if(entity instanceof Variable) {
  //      Type type = ((Variable) entity).nearestAncestor(Type.class);
  //      return new Label(type.getFullyQualifiedName()+"."+((Variable)entity).name());
  //    } else if(entity instanceof Namespace) {
  //      return new Label(((Namespace)entity).getFullyQualifiedName());
  //    } else if(entity instanceof UniEdge) {
  //      UniEdge<?> edge = (UniEdge) entity;
  //      return new Label(getText(edge.startNode().object()) + " has " + edge.get(DependencyCount.class).value()+" dependencies on "+getText(edge.endNode().object()));
  //    }
  //    return null;
  //  }
  //
  //  @Override
  //  public Color getNodeHighlightColor(Object entity) {
  //    return Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
  //  }
  //
  //  @Override
  //  public Color getBorderColor(Object entity) {
  //    return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
  //  }
  //
  //  @Override
  //  public Color getBorderHighlightColor(Object entity) {
  //    return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
  //  }
  //
  //  @Override
  //  public int getBorderWidth(Object entity) {
  //    return 1;
  //  }
  //
    @Override
    public Color getBackground(Object entity) {
      if(entity instanceof Node) {
        Node<Element> node = (Node<Element>)entity;
        if(node.canReachOther(node)) {
          return new Color(Display.getCurrent(), 200, 50, 50);
        }
      } 
      return Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION);
    }
  
    @Override
    public Color getForeground(Object entity) {
      return Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Map<String, Object> getNestedGraphAttributes(Object nestingNode) {
      return new HashMap<>();
    }
  
  //  @Override
  //  public boolean fisheyeNode(Object entity) {
  //    return false;
  //  }
  //
  //  @Override
  //  public ConnectionRouter getRouter(Object rel) {
  //    // Default implementation as per the javadocs of Zest 2.
  //    return null;
  //  }
  //
  //  @Override
  //  public IConnectionDecorator getConnectionDecorator(Object rel) {
  //    return new DirectedConnectionDecorator();
  //  }

}
