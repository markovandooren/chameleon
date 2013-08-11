package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.Map;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalyzer.GraphBuilder;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorExtension;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.action.TopDown;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;

import com.google.common.base.Function;

public class DependencyView extends ViewPart {

//	private Graph _graph;
	
//	private int _layout = 1;
	
//	public void createPartControl(Composite parent) {
//		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
//    mgr.add(new AnalyzeoDocumentTypeAction(parent));
//	}
	
	private GraphViewer _viewer;
	
	private class DependencyLabelProvider extends LabelProvider {
		
		
		@Override
		public String getText(Object element) {
			if(element instanceof Element) {
				return ((Element)element).language().plugin(EclipseEditorExtension.class).getLabel((Element) element);
			} else {
				return "";
			}
		}
		
		@Override
		public Image getImage(Object element) {
			if(element instanceof Element) {
				try {
				return ((Element)element).language().plugin(EclipseEditorExtension.class).getIcon((Element) element);
				} catch(ModelException exc) {
					exc.printStackTrace();
				}
			}
			return super.getImage(element);
		}
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		_viewer = new GraphViewer(parent, SWT.BORDER);
		_viewer.setContentProvider(new DependencyContentProvider());
		_viewer.setLabelProvider(new DependencyLabelProvider());
		// Start with an empty model.
		_viewer.setInput(new DependencyResult());
		_viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
		_viewer.applyLayout();
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
    mgr.add(new AnalyzeDocumentTypeAction(parent));
	}
	
	private ChameleonEditor editor() {
		ChameleonEditor editor = ChameleonEditor.getActiveEditor();
		return editor;
	}
	
	private class AnalyzeDocumentTypeAction extends Action {
		
		public AnalyzeDocumentTypeAction(Composite parent) {
			_parent = parent;
		}
		
		private Composite _parent;
		
		@Override
		public void run() {
//			if(_graph != null) {
////				for(GraphNode node: _nodeMap.values()) {
////					node.
////				}
//				_graph.dispose();
//			}
//			_graph = new Graph(_parent, SWT.NONE);
//	    _graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

//			ZESTGraphBuilder builder = new ZESTGraphBuilder<Type>(_graph, new LabelGenerator<Type>() {
//				@Override
//				public String text(Type t) {
//					return t.name();
//				}
//			});
			
			Function<Type,Type> identity = new Function<Type, Type>() {

				@Override
				public Type apply(Type type) {
					return type;
				}
			};
			
			DependencyAnalysis<Type, Type> analysis = 
					new DependencyAnalysis<Type,Type>(
							Type.class, new True(), 
							new True(), 
							Type.class, identity, new True(), 
							new True());
			
			ChameleonEditor editor = editor();
			Document document = editor.getDocument().document();
			TopDown<Element, Nothing> topDown = new TopDown<>(analysis);
			topDown.perform(document);
			
			DependencyResult result = analysis.result();

			_viewer.setInput(result);
			
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
	
//	private static class DependencyVisualizer<S extends Declaration, E extends S, T extends S> {
//		
//		public DependencyVisualizer(GraphBuilder<S> builder) {
//			_builder = builder;
//		}
//		
//		private GraphBuilder<S> _builder;
//		
//		public void visualize(DependencyResult<E, T> result) {
//		Map<E,Set<T>> deps = result.dependencies();
//		for(Map.Entry<E,Set<T>> dependencies: deps.entrySet()) {
//			E origin = dependencies.getKey();
//			_builder.addVertex(origin);
//			for(T dependency: dependencies.getValue()) {
//				_builder.addVertex(dependency);
//				_builder.addEdge(origin, dependency);
//			}
//		}
//	}
//
//	}
	
//  public void setLayoutManager() {
//    switch (_layout) {
//    case 1:
//      _graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
//      _layout++;
//      break;
//    case 2:
//      _graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
//      _layout = 1;
//      break;
//
//    }
//
//  }
	
	@Override
	public void setFocus() {
	}

}
