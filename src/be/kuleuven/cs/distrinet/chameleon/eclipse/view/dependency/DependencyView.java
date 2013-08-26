package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DefaultDependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Workbenches;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
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
				if(chameleonProject != null) {
					Control control = _stackMap.get(chameleonProject);
					if(control == null) {
						control = createConfigurationControls(_stack, chameleonProject);
						_stackMap.put(chameleonProject, control);
					} 
					((StackLayout)_stack.getLayout()).topControl = control;
				}
				_sourceTab = _sources.get(chameleonProject);
				_targetTab = _targets.get(chameleonProject);
				_crossReferenceTab = _crossReferences.get(chameleonProject);
				_dependencyTab = _dependencies.get(chameleonProject);
				_mapper = _mappers.get(chameleonProject);
				Display.getDefault().syncExec(new Runnable(){
					@Override
					public void run() {
						_stack.layout(true);
						DependencyView.this.controlContainer().layout(true);
					}
				});
			}
		}

	}
	
	private Map<Project,Control> _stackMap = new HashMap<>();

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
		initStack(parent);
		registerPartListener();
		
		
//		parent.addControlListener(new ControlListener(){
//		
//			@Override
//			public void controlResized(ControlEvent e) {
//				int width = ((Composite)e.widget).getBounds().width;
//				GridData layoutData = (GridData) _stack.getLayoutData();
//				if(layoutData != null) {
//					layoutData.widthHint = (int) (0.25 * width);
//				}
//			}
//		
//			@Override
//			public void controlMoved(ControlEvent e) {
//			}
//		});
		
		
	}
	
	private void initStack(Composite parent) {
		_stack = new Composite(parent, SWT.NONE);
		StackLayout layout = new StackLayout();
		_stack.setLayout(layout);
		_stack.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,true));
	}
	
	private Composite _stack;

	private Control createConfigurationControls(Composite parent, Project project) {
		final Composite right = new Composite(parent, SWT.NONE);
		GridData rightData = new GridData(GridData.FILL,GridData.FILL,true,true);
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

		createSourceTab(folder,project);
		createTargetTab(folder,project);
		createCrossReferenceTab(folder,project);
		createDependenciesTab(folder,project);
		//FIXME Filthy hack! Use the configuration options: create a new one and store it. It just
		//      needs to store its own selector lists.
		final Language language = project.views().get(0).language();
		DependencyOptions plugin = language.plugin(DependencyOptions.class);
		if(plugin == null) {
			plugin = new DefaultDependencyOptions();
		}
		DependencyConfiguration config = plugin.createConfiguration();
		_mappers.put(project, config.mapper());
		return right;
	}

	private void createSourceTab(TabFolder folder,Project project) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.sourceOptions();
			}
		};
		String name = "Source";
		_sources.put(project,createTab(folder, selector, name,project));
	}
	
	private Map<Project,SelectorList> _sources = new HashMap<>();
	private Map<Project,SelectorList> _targets= new HashMap<>();
	private Map<Project,SelectorList> _crossReferences= new HashMap<>();
	private Map<Project,SelectorList> _dependencies= new HashMap<>();
	private Map<Project,Function> _mappers= new HashMap<>();

	protected SelectorList createTab(TabFolder folder, Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector, String name, Project project) {
		TabItem tab = new TabItem(folder, SWT.NONE);
		tab.setText(name);
		final ScrolledComposite scroll = new ScrolledComposite(folder, SWT.V_SCROLL);
		Composite canvas = new Canvas(scroll,SWT.NONE);
		scroll.setContent(canvas);
		
		scroll.setExpandVertical(true);
		scroll.setExpandHorizontal(true);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		canvas.setLayout(layout);
		canvas.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		tab.setControl(scroll);
		SelectorList optionsTab = new SelectorList(canvas, SWT.NONE, selector,project);
		final GridData layoutData = new GridData(SWT.FILL,SWT.FILL,true,true);
		layoutData.widthHint = scroll.getClientArea().width;
		optionsTab.setLayoutData(layoutData);
//		canvas.addControlListener(new ControlListener(){
//		
//			@Override
//			public void controlResized(ControlEvent e) {
//				layoutData.widthHint = scroll.getClientArea().width;
//			}
//		
//			@Override
//			public void controlMoved(ControlEvent e) {
//			}
//		});
		return optionsTab;
	}

	private void createTargetTab(TabFolder folder,Project project) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.targetOptions();
			}
		};
		String name = "Target";
		_targets.put(project,createTab(folder, selector, name,project));
	}

	private void createCrossReferenceTab(TabFolder folder,Project project) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.crossReferenceOptions();
			}
		};
		String name = "Cross-Reference";
		_crossReferences.put(project,createTab(folder, selector, name,project));
	}

	private void createDependenciesTab(TabFolder folder,Project project) {
		Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector = new Function<DependencyConfiguration, List<PredicateSelector>, Nothing>() {
			@Override
			public List<PredicateSelector> apply(DependencyConfiguration argument) throws Nothing {
				return (List)argument.dependencyOptions();
			}
		};
		String name = "Dependency";
		_dependencies.put(project,createTab(folder, selector, name,project));
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
	private SelectorList _sourceTab;
	
	Function mapper() {
		return _mapper;
	}
	
	private Function _mapper;

	UniversalPredicate targetPredicate() {
		return _targetTab.predicate();
	}
	private SelectorList _targetTab;

	UniversalPredicate crossReferencePredicate() {
		return _crossReferenceTab.predicate();
	}
	private SelectorList _crossReferenceTab;
	
	UniversalPredicate dependencyPredicate() {
		return _dependencyTab.predicate();
	}
	private SelectorList _dependencyTab;

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



	private List<SelectorList> _optionTabs = new ArrayList<>();
}
