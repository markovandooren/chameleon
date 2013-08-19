package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Editors;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Projects;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Workbenches;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Workspaces;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class DependencyView extends ViewPart {

	private final class DependencyControlUpdater implements IPartListener {
		@Override
		public void partOpened(IWorkbenchPart part) {
		}

		@Override
		public void partDeactivated(IWorkbenchPart part) {
		}

		@Override
		public void partClosed(IWorkbenchPart part) {
		}

		@Override
		public void partBroughtToTop(IWorkbenchPart part) {
		}

		@Override
		public void partActivated(IWorkbenchPart part) {
			if(part instanceof IEditorPart) {
				Project chameleonProject = Workbenches.chameleonProject(part);
				if(chameleonProject != null) {
					populateOptionTabs(chameleonProject);
				} else {
					clearSourceTab();
				}
			}
		}

	}

	GraphViewer _viewer;

		private Composite _controlContainer;
		
		private Composite controlContainer() {
			return _controlContainer;
		}

	@Override
	public void createPartControl(Composite parent) {
		_controlContainer = parent;
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

		TabFolder folder = new TabFolder(right, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);

		createTargetTab(folder);
		createSourceTab(folder);
		
		registerPartListener();

	}

	private void createSourceTab(TabFolder folder) {
		TabItem sourceTab = new TabItem(folder, SWT.NONE);
		sourceTab.setText("Source");
		_sourceCanvas = new Canvas(folder,SWT.NONE);
		GridLayout sourceLayout = new GridLayout();
		sourceLayout.numColumns = 1;
		_sourceCanvas.setLayout(sourceLayout);
		sourceTab.setControl(_sourceCanvas);
	}

	private void createTargetTab(TabFolder folder) {
		TabItem targetTab = new TabItem(folder, SWT.NONE);
		targetTab.setText("Target");
		_targetCanvas = new Canvas(folder,SWT.NONE);
		GridLayout targetLayout = new GridLayout();
		targetLayout.numColumns = 1;
		_targetCanvas.setLayout(targetLayout);
		targetTab.setControl(_targetCanvas);
	}

	private void registerPartListener() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		activePage.addPartListener(new DependencyControlUpdater());
	}


	private List<PredicateSelector> _sourceSelectors = new ArrayList();

	UniversalPredicate sourcePredicate() {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: _sourceSelectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}

	private List<PredicateSelector> _targetSelectors = new ArrayList();

	UniversalPredicate targetPredicate() {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: _targetSelectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}

	private void clearSourceTab() {

		Display.getDefault().syncExec(new Runnable(){
		
			@Override
			public void run() {
				for(Control control:_sourceCanvas.getChildren()) {
					control.dispose();
				}
				for(Control control:_targetCanvas.getChildren()) {
					control.dispose();
				}
				_sourceSelectors.clear();
				_targetSelectors.clear();
			}
		});
	}

	private Composite _sourceCanvas;
	
	private Composite _targetCanvas;

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

	private void populateOptionTabs(final Project chameleonProject) {
		Display.getDefault().syncExec(new Runnable(){

			@Override
			public void run() {
				_sourceSelectors = new ArrayList<PredicateSelector>();
				final Language language = chameleonProject.views().get(0).language();
				DependencyOptions plugin = language.plugin(DependencyOptions.class);
				if(plugin == null) {
					plugin = new DefaultDependencyOptions();
				}
				for(PredicateSelector selector: plugin.sourceOptions()) {
					selector.createControl(_sourceCanvas);
					_sourceSelectors.add(selector);
				}
				for(PredicateSelector selector: plugin.targetOptions()) {
					selector.createControl(_targetCanvas);
					_targetSelectors.add(selector);
				}
				//				_sourceCanvas.redraw();
				DependencyView.this.controlContainer().layout(true);
			}
		});
	}

}