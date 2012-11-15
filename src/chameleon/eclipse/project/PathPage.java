package chameleon.eclipse.project;

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
import chameleon.workspace.ProjectConfig;

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
	
	Tree _sourceTree;
	
	TreeItem _sourceProjectRoot;
	
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
				_sourceProjectRoot.setText(wizard().projectName());
			}
		});
		
		TabFolder folder = new TabFolder(controlContainer, SWT.BORDER);
		GridData tabFolderGridData = new GridData(GridData.FILL,GridData.FILL,true,true);
		folder.setLayoutData(tabFolderGridData);
		
		createSourceTab(folder);
		
		TabItem binaryTab = new TabItem(folder, SWT.NONE);
		binaryTab.setText("Binary Path");
		
		
	}

	private ProjectConfig projectConfig() {
		return wizard().projectConfig();
	}
	
	private List<String> _sourcePaths = new ArrayList<>();
	
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
		
		Button directoryButton = new Button(sourceCanvas,SWT.PUSH);
		directoryButton.setText("Add directory");
		GridData directoryButtonGridData = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		directoryButton.setLayoutData(directoryButtonGridData);
		directoryButton.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent arg0) {
					DirectoryDialog sourceDirectoryDialog = new DirectoryDialog(getShell());
					sourceDirectoryDialog.setMessage("Select a source directory");
					String directory = sourceDirectoryDialog.open();
					addSourceLoader(directory);
					// add to the UI or listen to config
				}
		});
		getControl().addFocusListener(new FocusListener(){
		
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		
			@Override
			public void focusGained(FocusEvent arg0) {
				ProjectConfig projectConfig = wizard().projectConfig();
				if(projectConfig != _cached) {
					for(String directory:_sourcePaths) {
						projectConfig.addSource(directory);
					}
				}
			}
		});
	}
	
	private ProjectConfig _cached;

	protected void addSourceLoader(String directory) {
		if(directory != null) {
			_sourcePaths.add(directory);
			projectConfig().addSource(directory);
		}
	}				

	private Composite controlContainer;
}
