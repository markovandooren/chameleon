package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Workbenches;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.SWTWidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
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
				for(OptionsTab tab: _optionTabs) {  
					if(chameleonProject != null) {
						tab.populate(chameleonProject);
					} else {
						tab.clear();;
					}
				}
				Display.getDefault().syncExec(new Runnable(){
					@Override
					public void run() {
						DependencyView.this.controlContainer().layout(true);
					}
				});
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
	}

	private void createConfigurationControls(Composite parent) {
		final Composite right = new Composite(parent, SWT.NONE);
		GridData rightData = new GridData(GridData.FILL,GridData.FILL,false,true);
		right.setLayoutData(rightData);
		GridLayout rightLayout = new GridLayout();
		rightLayout.numColumns = 1;
		right.setLayout(rightLayout);

		createAnalyzeButton(right);
		
//		new SWTWidgetFactory() {
//			@Override
//			public Composite parent() {
//				return right;
//			}
//		}.createCheckboxList();

		TabFolder folder = new TabFolder(right, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);

		createTargetTab(folder);
		createCrossReferenceTab(folder);
		createSourceTab(folder);

		registerPartListener();

	}

	private void createSourceTab(TabFolder folder) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.sourceOptions();
			}
		};
		String name = "Source";
		_sourceTab = createTab(folder, selector, name);
		_optionTabs.add(_sourceTab);
	}

	protected OptionsTab createTab(TabFolder folder, Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector, String name) {
		TabItem tab = new TabItem(folder, SWT.NONE);
		tab.setText(name);
		ScrolledComposite scroll = new ScrolledComposite(folder, SWT.V_SCROLL);
		Composite canvas = new Canvas(scroll,SWT.NONE);
		scroll.setContent(canvas);
		
		scroll.setExpandVertical(true);
		scroll.setExpandHorizontal(true);
		scroll.setMinSize(canvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		canvas.setLayout(layout);
		tab.setControl(scroll);
		OptionsTab optionsTab = new OptionsTab(canvas, selector);
		return optionsTab;
	}

	private void createTargetTab(TabFolder folder) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.targetOptions();
			}
		};
		String name = "Target";
		_targetTab = createTab(folder, selector, name);
		_optionTabs.add(_targetTab);
	}

	private void createCrossReferenceTab(TabFolder folder) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.crossReferenceOptions();
			}
		};
		String name = "Cross-Reference";
		_crossReferenceTab = createTab(folder, selector, name);
		_optionTabs.add(_crossReferenceTab);
	}

	private void registerPartListener() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		activePage.addPartListener(new DependencyControlUpdater());
	}


	UniversalPredicate sourcePredicate() {
		return _sourceTab.predicate();
	}
	private OptionsTab _sourceTab;

	UniversalPredicate targetPredicate() {
		return _targetTab.predicate();
	}
	private OptionsTab _targetTab;

	UniversalPredicate crossReferencePredicate() {
		return _crossReferenceTab.predicate();
	}
	private OptionsTab _crossReferenceTab;

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
//				_viewer.setLayoutAlgorithm(new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
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



	private List<OptionsTab> _optionTabs = new ArrayList<>();
}
