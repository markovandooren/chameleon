package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyAnalysis;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.action.TopDown;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;

import com.google.common.base.Function;

public class DependencyView extends ViewPart {

	private GraphViewer _viewer;
	
//	private Composite _controlContainer;
//	
//	private Composite controlContainer() {
//		return _controlContainer;
//	}
	
	@Override
	public void createPartControl(Composite parent) {
//		_controlContainer = new Composite(parent,SWT.NONE);
		
//		setControl(controlContainer());
		GridLayout gridLayout = new GridLayout(2,false);
////		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);
		
//    Label label = new Label(parent, SWT.NONE);
//    label.setText("A label");
//    Button button = new Button(parent, SWT.PUSH);
//    button.setText("Press Me");
		
		addGraphViewer(parent);
		
		
		Composite right = new Composite(parent, SWT.NONE);
		GridData rightData = new GridData(GridData.FILL,GridData.FILL,false,true);
	  right.setLayoutData(rightData);
		GridLayout rightLayout = new GridLayout();
		rightLayout.numColumns = 1;
		right.setLayout(rightLayout);
		
	  addAnalyzeButton(right);
//	  Display display = new Display();
//	  analyze.setImage(display.getSystemImage(SWT.ICON_SEARCH));
		
		TabFolder folder = new TabFolder(right, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);
		TabItem sourceTab = new TabItem(folder, SWT.NONE);
		sourceTab.setText("Source");
		
		Canvas canvas = new Canvas(folder,SWT.NONE);
		GridLayout sourceLayout = new GridLayout();
		sourceLayout.numColumns = 1;
		canvas.setLayout(sourceLayout);
		sourceTab.setControl(canvas);
		
		Label customLabel = new Label(canvas, SWT.NONE);
		customLabel.setText("Analysis Settings");
		
//		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
//    mgr.add(new AnalyzeDocumentTypeAction());
	}

	protected void addGraphViewer(Composite parent) {
		_viewer = new GraphViewer(parent, SWT.NONE);
		_viewer.getGraphControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		_viewer.setContentProvider(new DependencyContentProvider());
		_viewer.setLabelProvider(new DependencyLabelProvider());
		// Start with an empty model.
		_viewer.setInput(new DependencyResult());
		SpringLayoutAlgorithm algorithm = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING + 
				ZestStyles.NODES_NO_LAYOUT_ANIMATION
				+ ZestStyles.NODES_NO_ANIMATION);
		_viewer.setLayoutAlgorithm(algorithm,true);
		// The following puts all nodes on top of each other. Rubbish layout.
//		_viewer.setLayoutAlgorithm(new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
//		_viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED + ZestStyles.CONNECTIONS_SOLID);
		_viewer.applyLayout();
	}

	protected void addAnalyzeButton(Composite right) {
		Button analyze = new Button(right, SWT.PUSH);
	  GridData analyzeData = new GridData();
	  analyzeData.horizontalAlignment = GridData.CENTER;
	  analyze.setLayoutData(analyzeData);
	  analyze.setText("Analyze");
	  MouseAdapter mouseAdapter = new MouseAdapter() {
	  	@Override
	  	public void mouseDown(MouseEvent e) {
	  		new AnalyzeDocumentTypeAction().run();
	  	}
		};
		analyze.addMouseListener(mouseAdapter);
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
			  ChameleonProjectNature nature = Projects.chameleonNature(project);
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
		
		@Override
		public void run() {
			try {
				final Document document = currentDocument();
				Job job = new Job("Dependency Analysis") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						final DependencyAnalysis<Type, Type> analysis = performAnalysis(document);				
						// If you want to update the UI
						UIJob uiJob = new UIJob("Dependency Analysis"){
							
							@Override
							public IStatus runInUIThread(IProgressMonitor monitor) {
								DependencyResult result = analysis.result();
								if(result != null) {
									_viewer.setInput(result);
								}
								return Status.OK_STATUS;
							}
							
						};
						uiJob.schedule();
						return Status.OK_STATUS;
					}
				};
				job.schedule(); 
			} catch(CoreException exc) {
				exc.printStackTrace();
			}


		}

		protected DependencyAnalysis<Type, Type> performAnalysis(Document document) {
			Function<Type,Type> identity = new Function<Type, Type>() {
				@Override
				public Type apply(Type type) {
					return type;
				}
			};
			
			final DependencyAnalysis<Type, Type> analysis = 
					new DependencyAnalysis<Type,Type>(
							Type.class, new True(), 
							new True(), 
							Type.class, identity, new True(), 
							new True());
			if(document != null) {
				TopDown<Element, Nothing> topDown = new TopDown<>(analysis);
				topDown.perform(document);

			}
			return analysis;
		}

	}
	
	@Override
	public void setFocus() {
	}

}
