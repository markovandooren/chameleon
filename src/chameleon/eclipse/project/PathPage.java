package chameleon.eclipse.project;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.connector.EclipseEditorExtension;
import chameleon.util.FileUtil;
import chameleon.workspace.ProjectConfiguration;

public class PathPage extends WizardPage implements IWizardPage {

	private static final String SET_SOURCE_PATH = "Set source path";

	public PathPage(String pageName, String title, ImageDescriptor titleImage, ProjectWizard wizard) {
		super(SET_SOURCE_PATH, title, titleImage);
		_wizard = wizard;
	}

	private ProjectWizard _wizard;

	public ProjectWizard wizard() {
		return _wizard;
	}

	public PathPage(ProjectWizard wizard) {
		super(SET_SOURCE_PATH);
		_wizard = wizard;
	}

	private Tree _sourceTree;

	private TreeItem _sourceProjectRoot;

	private String _cacheRoot;

	@Override
	public void createControl(Composite parent) {
		controlContainer = new Composite(parent,SWT.NONE);
		setControl(controlContainer);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		controlContainer.setLayout(gridLayout);

		Canvas root = new Canvas(controlContainer, SWT.NONE);
		root.addFocusListener(new FocusListener(){

			@Override
			public void focusLost(FocusEvent arg0) {
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				_initialized = true;
				_sourceProjectRoot.setText(wizard().projectName());


				ProjectConfiguration projectConfig = wizard().projectConfig();
				File file = wizard().projectDirectory();
				String projectPathName = file == null ? null: file.toString();
				if(file != null && ! projectPathName.equals(_cacheRoot)) {
					_sourceProjectRoot.removeAll();
					_cacheRoot = projectPathName;
					for(String directory:_sourcePaths) {
						projectConfig.addSource(directory);
						// This attaches the child to the tree
						createNode(_sourceProjectRoot);
					}
					addSrc();
				}
			}


		});

		TabFolder folder = new TabFolder(controlContainer, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);

		createSourceTab(folder);

		TabItem binaryTab = new TabItem(folder, SWT.NONE);
		binaryTab.setText("Binary Path");


	}

	protected void createNode(TreeItem parent) {
		createNode(parent,null);
	}

	protected void createNode(TreeItem parent, String text) {
		@SuppressWarnings("unused")
		TreeItem child = new TreeItem(parent,0);
		init(text, child);
	}

	protected void init(String text, TreeItem child) {
		if(text != null) {
			child.setText(text);
		}
		child.setExpanded(true);
		TreeItem parentItem = child.getParentItem();
		if(parentItem != null) {
			parentItem.setExpanded(true);
		}
	}

	/**
	 * Thanks Eclipse developers, for not putting an interface above Tree and TreeItem, but 
	 * forcing code duplication instead.
	 */
	protected void createNode(Tree parent, String text) {
		@SuppressWarnings("unused")
		TreeItem child = new TreeItem(parent,0);
		init(text, child);
	}

	private String relativePath(String path) {
		String root = wizard().projectRootPath().toString();
		return FileUtil.relativePath(root, path);
	}

	private ProjectConfiguration projectConfig() {
		return wizard().projectConfig();
	}

	private List<String> _sourcePaths = new ArrayList<String>();

	/**
	 * Create a tab to configure the source loaders of the project.
	 * @param folder
	 */
	protected void createSourceTab(TabFolder folder) {
		// 1. SOURCE TAB
		TabItem sourceTab = new TabItem(folder, SWT.NONE);
		sourceTab.setText("Source Path");
		// 1.A Source canvas
		Canvas sourceCanvas = new Canvas(folder, SWT.NONE);
		GridLayout sourceLayout = new GridLayout();
		sourceLayout.numColumns = 2;
		sourceCanvas.setLayout(sourceLayout);
		sourceTab.setControl(sourceCanvas);

		_sourceTree = new Tree(sourceCanvas,SWT.BORDER);
		GridData sourceTreeGridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		sourceTreeGridData.verticalSpan = 2;
		_sourceTree.setLayoutData(sourceTreeGridData);
		_sourceProjectRoot = new TreeItem(_sourceTree, 0);
		_sourceProjectRoot.setText(INFORMATION, "project root");
		_sourceProjectRoot.setExpanded(true);

		Image image = null;
		try {
			image = EclipseEditorExtension.loadIcon("chameleon.png", ChameleonEditorPlugin.PLUGIN_ID);
		} catch (MalformedURLException e) {
		}
		_sourceProjectRoot.setImage(image);

		Button addDirectoryButton = new Button(sourceCanvas,SWT.PUSH);
		addDirectoryButton.setText("Add directory");
		GridData directoryButtonGridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		addDirectoryButton.setLayoutData(directoryButtonGridData);
		addDirectoryButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				DirectoryDialog sourceDirectoryDialog = new DirectoryDialog(getShell());
				sourceDirectoryDialog.setMessage("Select a source directory");
				sourceDirectoryDialog.setFilterPath(wizard().projectDirectory().toString());
				String absoluteDirectory = sourceDirectoryDialog.open();
				if(absoluteDirectory != null) {
					String directory = relativePath(absoluteDirectory);
					if(directory.equals("")) {
						directory = ".";
					}
					addSourceLoader(directory);
				}
			}
		});

		Button removeDirectoryButton = new Button(sourceCanvas,SWT.PUSH);
		removeDirectoryButton.setText("Remove directory");
		GridData removeDirectoryButtonGridData = new GridData(GridData.FILL, GridData.BEGINNING, false, false);
		removeDirectoryButton.setLayoutData(removeDirectoryButtonGridData);
		removeDirectoryButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				for(TreeItem selected:_sourceTree.getSelection()) {
					// Only remove the item if it is not the project root.
					if(selected != _sourceProjectRoot) {
						selected.dispose();
					}
				}
			}
		});



		// When the tab is selected and the project has changed, we sync the tree
		// with the configuration of the project.
		//		getControl().addFocusListener(new FocusAdapter() {
		//			@Override
		//			public void focusGained(FocusEvent arg0) {
		//				ProjectConfig projectConfig = wizard().projectConfig();
		//				_sourceProjectRoot.removeAll();
		//				if(projectConfig != _cached) {
		//					_cached = projectConfig;
		//					for(String directory:_sourcePaths) {
		//						projectConfig.addSource(directory);
		//						// This attaches the child to the tree
		//						@SuppressWarnings("unused")
		//						TreeItem child = new TreeItem(_sourceProjectRoot,0);
		//					}
		//					try {
		//						IFolder f = wizard().project().getFolder("src");
		//						f.create(true, true, new NullProgressMonitor());
		//						addSourceLoader("src");
		//					} catch (CoreException e) {
		//						System.out.println("Could not create src directory");
		//					}
		//				}
		//			}
		//		});
	}

	private ProjectConfiguration _cached;

	protected void addSourceLoader(String directory) {
		if(directory != null) {
			_sourcePaths.add(directory);
			projectConfig().addSource(directory);
		}
		File file = new File(directory);
		TreeItem child;
		if(file.isAbsolute()) {
			createNode(_sourceTree, directory);
		} else {
			createNode(_sourceProjectRoot, directory);
		}
	}				

	private Composite controlContainer;

	private boolean _initialized = false;

	public void complete() {
		if(! _initialized) {
			addSrc();
		}
	}

	protected void addSrc() {
		File file = wizard().projectDirectory();
		String projectPathName = file == null ? null: file.toString();
		if(projectPathName != null) {
			try {
				File src = new File(projectPathName + File.separator + "src");
				if(! src.exists()) {
					src.mkdirs();
				}
				if(src.isDirectory()) {
					addSourceLoader("src");
				}
			} catch (Exception e) {
				System.out.println("Could not create src directory");
			}
		}
	}

}
