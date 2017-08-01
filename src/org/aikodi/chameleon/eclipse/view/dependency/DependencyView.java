package org.aikodi.chameleon.eclipse.view.dependency;
//
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aikodi.chameleon.analysis.AnalysisOptions;
import org.aikodi.chameleon.analysis.OptionGroup;
import org.aikodi.chameleon.analysis.dependency.DefaultDependencyOptionsFactory;
import org.aikodi.chameleon.analysis.dependency.DependencyOptionsFactory;
import org.aikodi.chameleon.analysis.dependency.DependencyResult;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.util.Workbenches;
import org.aikodi.chameleon.workspace.Project;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.layout.ILayoutAlgorithm;
import org.eclipse.gef.layout.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.gef.layout.algorithms.HorizontalShiftAlgorithm;
import org.eclipse.gef.layout.algorithms.SpringLayoutAlgorithm;
import org.eclipse.gef.layout.algorithms.SugiyamaLayoutAlgorithm;
import org.eclipse.gef.zest.fx.jface.ZestContentViewer;
import org.eclipse.gef.zest.fx.jface.ZestFxJFaceModule;
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

public class DependencyView extends ViewPart {//implements IZoomableWorkbenchPart {

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
	
	private ZestContentViewer _viewer;
	
//	GraphViewer _viewer;

	private Composite _controlContainer;

	private Composite controlContainer() {
		return _controlContainer;
	}

	@Override
	public void createPartControl(Composite parent) {
		_controlContainer = parent;
		GridLayout gridLayout = new GridLayout(2,false);
		parent.setLayout(gridLayout);
		addZest2GraphViewer(parent);
		initStack(parent);
		registerPartListener();
		
		
	}
	
	private Map<String, ILayoutAlgorithm> _layouts = new HashMap<>();
	
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
		return optionsTab;
	}

	private void registerPartListener() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		activePage.addPartListener(new DependencyControlUpdater());
	}


	private void registerLayout(String name, ILayoutAlgorithm algorithm) {
		_layouts.put(name, algorithm);
		_layoutList.add(name);

	}
	
	protected void addZest2GraphViewer(Composite parent) {
	  ZestContentViewer viewer = new ZestContentViewer(new ZestFxJFaceModule());
	  viewer.createControl(parent, SWT.NONE);
	  SugiyamaLayoutAlgorithm layoutAlgorithm = new SugiyamaLayoutAlgorithm();
	  registerLayout("Sugiyama", layoutAlgorithm);
	  viewer.setContentProvider(new DependencyContentProvider());
	  viewer.setLabelProvider(new DependencyLabelProvider());
	  viewer.setLayoutAlgorithm(layoutAlgorithm);
	  viewer.setInput(new DependencyResult());
	  viewer.getControl().setLayoutData(new GridData( SWT.FILL, SWT.FILL, true, true));
	  _viewer = viewer;
//		_viewer2 = new org.eclipse.gef4.zest.core.viewers.GraphViewer(parent, SWT.NONE);
//		GraphWidget graphControl = _viewer2.getGraphControl();
//		graphControl.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
		
		
		
		
//		_viewer2.setContentProvider(new DependencyContentProvider());
//		_viewer2.setInput(new DependencyResult());
//		_viewer2.setLabelProvider(new DependencyLabelProvider());
		
//		String directedName = "Directed";
//		org.eclipse.gef4.layout.algorithms.
//		DirectedGraphLayoutAlgorithm directed = new DirectedGraphLayoutAlgorithm();
		// The new directed layout algorithm seems to work better without the
		// horizontal shift.
		
//		CompositeLayoutAlgorithm directedAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						directed,
//						new HorizontalShiftAlgorithm()
//				});
//		registerLayout(directedName, directed);

//		SpaceTreeLayoutAlgorithm spaceTreeAlgorithm=new SpaceTreeLayoutAlgorithm();
//		
//		SugiyamaLayoutAlgorithm sugiyamaAlgorithm = new SugiyamaLayoutAlgorithm();
////		SugiyamaLayoutAlgorithm sugiyama = new SugiyamaLayoutAlgorithm();
////		CompositeLayoutAlgorithm sugiyamaAlgorithm = new CompositeLayoutAlgorithm(
////				new LayoutAlgorithm[]{
////						sugiyama,
////						new HorizontalShiftAlgorithm()
////				});
//		
//		RadialLayoutAlgorithm radial = new RadialLayoutAlgorithm();
//		CompositeLayoutAlgorithm radialAlgorithm = new CompositeLayoutAlgorithm(
//				new LayoutAlgorithm[]{
//						radial,
//						new HorizontalShiftAlgorithm()
//				});
//		
		SpringLayoutAlgorithm spring = new SpringLayoutAlgorithm();
		CompositeLayoutAlgorithm springAlgorithm = new CompositeLayoutAlgorithm(
				new ILayoutAlgorithm[]{
						spring,
						new HorizontalShiftAlgorithm()
				});
		registerLayout("Spring", springAlgorithm);
//		registerLayout("Radial", radialAlgorithm);
//		registerLayout("Sugiyama", sugiyamaAlgorithm);
//		registerLayout("Space Tree", spaceTreeAlgorithm);
//
////		String spaceTreeName = "Space Tree";
////		SpaceTreeLayoutAlgorithm spaceTree = new SpaceTreeLayoutAlgorithm();
////		CompositeLayoutAlgorithm spaceTreeAlgorithm = new CompositeLayoutAlgorithm(
////				new LayoutAlgorithm[]{
////						spaceTree,
////						new HorizontalShiftAlgorithm()
////				});
////		_layouts.put(spaceTreeName, spaceTreeAlgorithm);
////		_layoutList.add(spaceTreeName);
//
//		// Only here because t
//		org.eclipse.gef4.layout.LayoutAlgorithm algorithm = (org.eclipse.gef4.layout.LayoutAlgorithm)springAlgorithm;
//		graphControl.setLayoutAlgorithm(algorithm, true);
//		_viewer2.applyLayout();
	}
		
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
						_viewer.setLayoutAlgorithm(_layouts.get(_layoutList.get(combo.getSelectionIndex())));
						_viewer.refresh();
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

//	@Override
//	public AbstractZoomableViewer getZoomableViewer() {
//		return _viewer;
//	}

  /**
   * @param result
   */
  public void setInput(DependencyResult result) {
    _viewer.setInput(null);
    _viewer.setInput(result);
  }

}
