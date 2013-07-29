package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import com.google.common.base.Function;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalyzer.GraphBuilder;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;

public class DependencyView extends ViewPart {

	private Graph _graph;
	
	private Map<Type,GraphNode> _nodeMap;
	
	private int _layout = 1;
	
//	public void createPartControl(Composite parent) {
//		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
//    mgr.add(new AnalyzeoDocumentTypeAction(parent));
//	}
	
	private GraphViewer _viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		_viewer = new GraphViewer(parent, SWT.BORDER);
		_viewer.setContentProvider(new DependencyContentProvider());
		
		// I think this should trigger the update.
		_viewer.setInput(model);
		_viewer.setLayoutAlgorithm(null,true);
		_viewer.applyLayout();
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
    mgr.add(new AnalyzeoDocumentTypeAction(parent));
	}
	
	
	
	private class AnalyzeoDocumentTypeAction extends Action {
		
		public AnalyzeoDocumentTypeAction(Composite parent) {
			_parent = parent;
		}
		
		private Composite _parent;
		
		@Override
		public void run() {
			if(_graph != null) {
//				for(GraphNode node: _nodeMap.values()) {
//					node.
//				}
				_graph.dispose();
			}
			_nodeMap = new HashMap<>();
			_graph = new Graph(_parent, SWT.NONE);
	    _graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

			ZESTGraphBuilder builder = new ZESTGraphBuilder<Type>(_graph, new LabelGenerator<Type>() {
				@Override
				public String text(Type t) {
					return t.name();
				}
			}, _nodeMap);
			
			Function<Type,Type> identity = new Function<Type, Type>() {

				@Override
				public Type apply(Type type) {
					return type;
				}
			};
			
			DependencyAnalysis<Type, Type> analysis = 
					new DependencyAnalysis<Type,Type>(
							Type.class, Type.class, 
							new True<Pair<Type,Type>>(), new True<CrossReference<?>>(), identity);
			
			ChameleonEditor editor = ChameleonEditor.getActiveEditor();
			Document document = editor.getDocument().document();
			document.apply(analysis);
			
			DependencyResult<Type, Type> result = analysis.result();
			
			new DependencyVisualizer<Type,Type,Type>(builder).visualize(result);
			
			_parent.redraw();
			
	    // Selection listener on graphConnect or GraphNode is not supported
	    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
//	    _graph.addSelectionListener(new SelectionAdapter() {
//	      @Override
//	      public void widgetSelected(SelectionEvent e) {
//	        System.out.println(e);
//	      }
	//
//	    });
		}
		
	}
	
	private static class DependencyVisualizer<S extends Declaration, E extends S, T extends S> {
		
		public DependencyVisualizer(GraphBuilder<S> builder) {
			_builder = builder;
		}
		
		private GraphBuilder<S> _builder;
		
		public void visualize(DependencyResult<E, T> result) {
		Map<E,Set<T>> deps = result.dependencies();
		for(Map.Entry<E,Set<T>> dependencies: deps.entrySet()) {
			E origin = dependencies.getKey();
			_builder.addVertex(origin);
			for(T dependency: dependencies.getValue()) {
				_builder.addVertex(dependency);
				_builder.addEdge(origin, dependency);
			}
		}
	}

	}
	
	interface LabelGenerator<T> {
		public String text(T t);
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
