package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
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
    mgr.add(new AnalyzeDocumentTypeAction());
	}
	
	private ChameleonEditor editor() {
		ChameleonEditor editor = ChameleonEditor.getActiveEditor();
		return editor;
	}
	
	private Document currentDocument() throws CoreException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IEditorInput editorInput = activeWorkbenchWindow.getActivePage().getActiveEditor().getEditorInput();
		Document document = null;
		if(editorInput instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput)editorInput).getFile();
			IProject project = file.getProject();
			if(project != null) {
			  ChameleonProjectNature nature = (ChameleonProjectNature) project.getNature(ChameleonProjectNature.NATURE);
			  if(nature != null) {
			  	document = nature.chameleonDocumentOfFile(file);
			  }
			}
		}
		return document;
	}
	
  IResource extractSelection(ISelection sel) {
    if (!(sel instanceof IStructuredSelection))
       return null;
    IStructuredSelection ss = (IStructuredSelection) sel;
    Object element = ss.getFirstElement();
    if (element instanceof IResource)
       return (IResource) element;
    if (!(element instanceof IAdaptable))
       return null;
    IAdaptable adaptable = (IAdaptable)element;
    Object adapter = adaptable.getAdapter(IResource.class);
    return (IResource) adapter;
 }
	
	private class AnalyzeDocumentTypeAction extends Action {
		
		public AnalyzeDocumentTypeAction() {
		}
		
		@Override
		public void run() {
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
			try {
				Document document = currentDocument();
//				ChameleonEditor editor = editor();
//				Document document = editor.getDocument().document();
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
			} catch(CoreException exc) {
				exc.printStackTrace();
			}
		}

	}
	
	@Override
	public void setFocus() {
	}

}
