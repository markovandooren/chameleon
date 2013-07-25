package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

public class DependencyView extends ViewPart {

	private Graph _graph;
	
	private int _layout = 1;
	
	@Override
	public void createPartControl(Composite parent) {
		_graph = new Graph(parent, SWT.NONE);
		
    GraphNode node1 = new GraphNode(_graph, SWT.NONE, "Jim");
    GraphNode node2 = new GraphNode(_graph, SWT.NONE, "Jack");
    GraphNode node3 = new GraphNode(_graph, SWT.NONE, "Joe");
    GraphNode node4 = new GraphNode(_graph, SWT.NONE, "Bill");
    // Lets have a directed connection
    new GraphConnection(_graph, ZestStyles.CONNECTIONS_DIRECTED, node1,
        node2);
    // Lets have a dotted graph connection
    new GraphConnection(_graph, ZestStyles.CONNECTIONS_DOT, node2, node3);
    // Standard connection
    new GraphConnection(_graph, SWT.NONE, node3, node1);
    // Change line color and line width
    GraphConnection graphConnection = new GraphConnection(_graph, SWT.NONE,
        node1, node4);
    graphConnection.changeLineColor(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));
    // Also set a text
    graphConnection.setText("This is a text");
    graphConnection.setHighlightColor(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
    graphConnection.setLineWidth(3);

    _graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
    // Selection listener on graphConnect or GraphNode is not supported
    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
    _graph.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        System.out.println(e);
      }

    });
	}

  public void setLayoutManager() {
    switch (_layout) {
    case 1:
      _graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      _layout++;
      break;
    case 2:
      _graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
      _layout = 1;
      break;

    }

  }
	
	@Override
	public void setFocus() {
	}

}
