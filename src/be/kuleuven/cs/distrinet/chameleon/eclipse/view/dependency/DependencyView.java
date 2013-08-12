package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.action.TopDown;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;

import com.google.common.base.Function;

public class DependencyView extends ViewPart {

	private GraphViewer _viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		_viewer = new GraphViewer(parent, SWT.BORDER);
		_viewer.setContentProvider(new DependencyContentProvider());
		_viewer.setLabelProvider(new DependencyLabelProvider());
		// Start with an empty model.
		_viewer.setInput(new DependencyResult());
		SpringLayoutAlgorithm algorithm = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		_viewer.setLayoutAlgorithm(algorithm,true);
		// The following puts all nodes on top of each other. Rubbish layout.
//		_viewer.setLayoutAlgorithm(new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
//		_viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED + ZestStyles.CONNECTIONS_SOLID);
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
	
	@Override
	public void setFocus() {
	}

}
