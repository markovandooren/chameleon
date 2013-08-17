package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.di.UISynchronize;
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
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;

public class DependencyView extends ViewPart {

	GraphViewer _viewer;
	
//	private Composite _controlContainer;
//	
//	private Composite controlContainer() {
//		return _controlContainer;
//	}
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout(2,false);
		parent.setLayout(gridLayout);
		
		addGraphViewer(parent);
		
		
		createConfigurationControls(parent);
		
//		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
//    mgr.add(new AnalyzeDocumentTypeAction());
	}

private void createConfigurationControls(Composite parent) {
	Composite right = new Composite(parent, SWT.NONE);
	GridData rightData = new GridData(GridData.FILL,GridData.FILL,false,true);
	right.setLayoutData(rightData);
	GridLayout rightLayout = new GridLayout();
	rightLayout.numColumns = 1;
	right.setLayout(rightLayout);
	
	createAnalyzeButton(right);
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

	for(int i =1; i<20;i++) {
	Label customLabel = new Label(canvas, SWT.NONE);
	customLabel.setText("Analysis Settings");
	}
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

	protected void createAnalyzeButton(Composite right) {
		Button analyze = new Button(right, SWT.PUSH);
	  GridData analyzeData = new GridData();
	  analyzeData.horizontalAlignment = GridData.CENTER;
	  analyze.setLayoutData(analyzeData);
	  analyze.setText("Analyze");
	  MouseAdapter mouseAdapter = new MouseAdapter() {
	  	@Override
	  	public void mouseDown(MouseEvent e) {
	  		IEditorInput input = input();
	  		new AnalyseDependencies(DependencyView.this, input).run();
	  	}
		};
		analyze.addMouseListener(mouseAdapter);
	}
	
	IEditorInput input() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorInput editorInput = activeWorkbenchWindow.getActivePage().getActiveEditor().getEditorInput();
		return editorInput;
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
  
	@Override
	public void setFocus() {
	}

}
