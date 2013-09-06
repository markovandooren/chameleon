package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef4.zest.layouts.LayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.HorizontalShiftAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.SpaceTreeLayoutAlgorithm;
import org.eclipse.gef4.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
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
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;

import be.kuleuven.cs.distrinet.chameleon.analysis.AnalysisOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.OptionGroup;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DefaultDependencyOptionsFactory;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyOptionsFactory;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyResult;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.util.Workbenches;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;

public class DependencyView extends ViewPart implements IZoomableWorkbenchPart {

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
					_project = chameleonProject;
					Control control = _stackMap.get(chameleonProject);
					if(control == null) {
						control = createConfigurationControls(_stack, chameleonProject);
						_stackMap.put(chameleonProject, control);
					} 
					((StackLayout)_stack.getLayout()).topControl = control;
				}
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

	private Map<Project, AnalysisOptions> _optionsMap = new HashMap<>();
	
	private Project _project;
	
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
//		addGraphViewer(parent);
		addZest2GraphViewer(parent);
		initStack(parent);
		registerPartListener();
		
		
//    ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
//    IActionBars bars = getViewSite().getActionBars();
//    bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}
	
	private Map<String, org.eclipse.gef4.zest.layouts.LayoutAlgorithm> _layouts = new HashMap<>();
	
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

		Composite buttonComposite = new Composite(right, SWT.NONE);
		GridLayout buttonCompositeLayout = new GridLayout();
		buttonCompositeLayout.numColumns = 2;
		buttonComposite.setLayout(buttonCompositeLayout);
		
		createAnalyzeButton(buttonComposite);
		createLayoutSelector(buttonComposite);
		
//		new SWTWidgetFactory() {
//			@Override
//			public Composite parent() {
//				return right;
//			}
//		}.createCheckboxList();

		TabFolder folder = new TabFolder(right, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);

		createTabs(folder, project);
		
		return right;
	}

	private void createTabs(TabFolder folder,Project project) {
		Language language = project.views().get(0).language();
		DependencyOptionsFactory plugin = language.plugin(DependencyOptionsFactory.class);
		if(plugin == null) {
			plugin = new DefaultDependencyOptionsFactory();
		}
		AnalysisOptions<?,?> options = plugin.createConfiguration();
		_optionsMap.put(project, options);
		for(OptionGroup group: options.optionGroups()) {
			createTab(folder, group, project);
		}
		options.setContext(project);
	}
	
//	private void createSourceTab(TabFolder folder,Project project) {
////		Function<DependencyOptions, List<PredicateSelector>, Nothing> selector = new Function<DependencyOptions, List<PredicateSelector>, Nothing>() {
////			@Override
////			public List<PredicateSelector> apply(DependencyOptions argument) throws Nothing {
////				return (List)argument.sourceOptions();
////			}
////		};
//		String name = "Source";
//		_sources.put(project,createTab(folder, selector, name,project));
//	}
	
//	private Map<Project,SelectorList> _sources = new HashMap<>();
//	private Map<Project,SelectorList> _targets= new HashMap<>();
//	private Map<Project,SelectorList> _crossReferences= new HashMap<>();
//	private Map<Project,SelectorList> _dependencies= new HashMap<>();
//	private Map<Project,Function> _mappers= new HashMap<>();

	protected SelectorList createTab(TabFolder folder, OptionGroup group, Project project) {
		TabItem tab = new TabItem(folder, SWT.NONE);
		tab.setText(group.name());
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
		SelectorList optionsTab = new SelectorList(canvas, SWT.NONE, group,project);
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

//	private void createTargetTab(TabFolder folder,Project project) {
//		Function<DependencyOptions, List<PredicateSelector>, Nothing> selector = new Function<DependencyOptions, List<PredicateSelector>, Nothing>() {
//			@Override
//			public List<PredicateSelector> apply(DependencyOptions argument) throws Nothing {
//				return (List)argument.targetOptions();
//			}
//		};
//		String name = "Target";
//		_targets.put(project,createTab(folder, selector, name,project));
//	}

//	private void createCrossReferenceTab(TabFolder folder,Project project) {
//		Function<DependencyOptions, List<PredicateSelector>, Nothing> selector = new Function<DependencyOptions, List<PredicateSelector>, Nothing>() {
//			@Override
//			public List<PredicateSelector> apply(DependencyOptions argument) throws Nothing {
//				return (List)argument.crossReferenceOptions();
//			}
//		};
//		String name = "Cross-Reference";
//		_crossReferences.put(project,createTab(folder, selector, name,project));
//	}
//
//	private void createDependenciesTab(TabFolder folder,Project project) {
//		Function<DependencyOptions, List<PredicateSelector>, Nothing> selector = new Function<DependencyOptions, List<PredicateSelector>, Nothing>() {
//			@Override
//			public List<PredicateSelector> apply(DependencyOptions argument) throws Nothing {
//				return (List)argument.dependencyOptions();
//			}
//		};
//		String name = "Dependency";
//		_dependencies.put(project,createTab(folder, selector, name,project));
//	}

	private void registerPartListener() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		activePage.addPartListener(new DependencyControlUpdater());
	}


//	UniversalPredicate sourcePredicate() {
//		return _sourceTab.predicate();
//	}
//	private SelectorList _sourceTab;
//	
//	Function mapper() {
//		return _mapper;
//	}
//	
//	private Function _mapper;
//
//	UniversalPredicate targetPredicate() {
//		return _targetTab.predicate();
//	}
//	private SelectorList _targetTab;
//
//	UniversalPredicate crossReferencePredicate() {
//		return _crossReferenceTab.predicate();
//	}
//	private SelectorList _crossReferenceTab;
//	
//	UniversalPredicate dependencyPredicate() {
//		return _dependencyTab.predicate();
//	}
//	private SelectorList _dependencyTab;

	protected void addZest2GraphViewer(Composite parent) {
		_viewer2 = new org.eclipse.gef4.zest.core.viewers.GraphViewer(parent, SWT.NONE);
		_viewer2.getGraphControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		_viewer2.setContentProvider(new DependencyContentProvider());
		_viewer2.setInput(new DependencyResult());
		_viewer2.setLabelProvider(new DependencyLabelProvider());
		
		String directedName = "Directed";
		DirectedGraphLayoutAlgorithm directed = new DirectedGraphLayoutAlgorithm();
		CompositeLayoutAlgorithm directedAlgorithm = new CompositeLayoutAlgorithm(
				new LayoutAlgorithm[]{
						directed,
						new HorizontalShiftAlgorithm()
				});
		_layouts.put(directedName, directedAlgorithm);
		_layoutList.add(directedName);

//		String sugiyamaName = "Sugiyama";
//		DirectedGraphLayoutAlgorithm sugiyama = new DirectedGraphLayoutAlgorithm();
//		CompositeLayoutAlgorithm sugiyamaAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						sugiyama,
//						new HorizontalShiftAlgorithm()
//				});
//		_layouts.put(sugiyamaName, sugiyamaAlgorithm);
//		_layoutList.add(sugiyamaName);
		
		RadialLayoutAlgorithm radial = new RadialLayoutAlgorithm();
		String radialName = "Radial";
		CompositeLayoutAlgorithm radialAlgorithm = new CompositeLayoutAlgorithm(
				new LayoutAlgorithm[]{
						radial,
						new HorizontalShiftAlgorithm()
				});
		_layouts.put(radialName, radialAlgorithm);
		_layoutList.add(radialName);

		String springName = "Spring";
		SpringLayoutAlgorithm spring = new SpringLayoutAlgorithm();
		CompositeLayoutAlgorithm springAlgorithm = new CompositeLayoutAlgorithm(
				new LayoutAlgorithm[]{
						spring,
						new HorizontalShiftAlgorithm()
				});
		_layouts.put(springName, springAlgorithm);
		_layoutList.add(springName);

//		String spaceTreeName = "Space Tree";
//		SpaceTreeLayoutAlgorithm spaceTree = new SpaceTreeLayoutAlgorithm();
//		CompositeLayoutAlgorithm spaceTreeAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						spaceTree,
//						new HorizontalShiftAlgorithm()
//				});
//		_layouts.put(spaceTreeName, spaceTreeAlgorithm);
//		_layoutList.add(spaceTreeName);

		_viewer2.getGraphControl().setLayoutAlgorithm(directed, true);
		_viewer2.applyLayout();
	}
	
	org.eclipse.gef4.zest.core.viewers.GraphViewer _viewer2;
	
	
//	protected void addGraphViewer(Composite parent) {
//		_viewer = new GraphViewer(parent, SWT.NONE);
//		_viewer.getGraphControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
//		_viewer.setContentProvider(new DependencyContentProvider());
//		_viewer.setLabelProvider(new DependencyLabelProvider());
//		// Start with an empty model.
//		_viewer.setInput(new DependencyResult());
//
//////	int style = LayoutStyles.NONE;
//		
//		// SPRING
//		int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
//		SpringLayoutAlgorithm spring = new SpringLayoutAlgorithm(
//				style 
//				);
//		;
//		spring.setSpringLength(SpringLayoutAlgorithm.DEFAULT_SPRING_LENGTH * 2);
//		CompositeLayoutAlgorithm springAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						spring,
//						new HorizontalShift(style)
//				});
//
//		String springName = "Spring";
//		_layouts.put(springName, springAlgorithm);
//		_layoutList.add(springName);
//		DirectedGraphLayoutAlgorithm directed = new DirectedGraphLayoutAlgorithm(style);
//		CompositeLayoutAlgorithm directedAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						directed,
//						new HorizontalShift(style)
//				});
//		String directedName = "Directed";
//		_layouts.put(directedName, directedAlgorithm);
//		_layoutList.add(directedName);
//		
//		RadialLayoutAlgorithm radial = new RadialLayoutAlgorithm(style);
//		String radialName = "Radial";
//		CompositeLayoutAlgorithm radialAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						radial,
//						new HorizontalShift(style)
//				});
//		_layouts.put(radialName, radialAlgorithm);
//		_layoutList.add(radialName);
//		
//		TreeLayoutAlgorithm tree = new RadialLayoutAlgorithm(style);
//		String treeName = "Tree";
//		CompositeLayoutAlgorithm treeAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						tree,
//						new HorizontalShift(style)
//				});
//		_layouts.put(treeName, treeAlgorithm);
//		_layoutList.add(treeName);
//		
//		
//		_viewer.setLayoutAlgorithm(springAlgorithm,true);
//		_viewer.applyLayout();
//	}
	
	private List<String>_layoutList = new ArrayList<>(); 

	protected void createLayoutSelector(Composite right) {
		final Combo combo = new Combo(right, SWT.NONE);
		combo.setItems(_layoutList.toArray(new String[_layoutList.size()]));
		combo.select(0);
		combo.addSelectionListener(new SelectionListener(){
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().syncExec(new Runnable(){
					@Override
					public void run() {
						_viewer2.setLayoutAlgorithm(_layouts.get(_layoutList.get(combo.getSelectionIndex())),true);
						_viewer2.applyLayout();
					}
				});
			}
		
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
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
				new AnalyseDependencies(_optionsMap.get(_project),DependencyView.this,input).run();
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

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return _viewer;
	}

}
