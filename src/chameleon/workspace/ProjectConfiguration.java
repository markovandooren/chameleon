package chameleon.workspace;

import java.io.File;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import chameleon.core.language.Language;
import chameleon.exception.ChameleonProgrammerException;

/**
 * A ProjectConfig mirrors the configuration of a Chameleon project. To load a project,
 * a {@link BootstrapProjectConfig} object is created. The bootstrapper is given the root
 * directory of the project (the directory in which project.xml is found), and a {@link LanguageRepository}.
 * The bootstrapper reads the language from the XML file by reading the XML nodes, and looks up the corresponding {@link Language} object
 * in the language repository. The bootstrapper gets the {@link ProjectConfigurator} plugin via {@link Language#plugin(Class)} and
 * invokes {@link ProjectConfigurator#createConfigElement(String, File, ProjectInitialisationListener)} to create an object of 
 * the appropriate subclass of ProjectConfig. In the last step, the bootstrapper tells the project config to process the
 * non-language nodes in the configuration file (only the language node is used by the bootstrapper itself).
 *  
 * @author Marko van Dooren
 */
public abstract class ProjectConfiguration extends ConfigElement {

	/**
	 * Create a new project configuration object. The new project configuration is
	 * refers to the given view and uses the given file input source factory to
	 * load files in source directories.
	 * @param view
	 * @param factory
	 * @param projectName
	 * @param root
	 */
	public ProjectConfiguration(View view, FileInputSourceFactory factory, String projectName, File root) {
		this(projectName, root, view, factory);
	}

	/**
	 * Create a new project configuration object for a project with the given name. 
	 * The new project configuration refers to the given view and uses the given 
	 * file input source factory to load files in source directories.
	 * @param projectName
	 * @param view
	 * @param factory
	 * @param root
	 */
	public ProjectConfiguration(String projectName, View view, FileInputSourceFactory factory, File root) {
		this(projectName, root, view, factory);
	}

	/**
	 * Create a new project configuration object for a project with the given name
	 * and the given project root directory. A new {@link Project} object is created
	 * and the given view is added to the project. The new project configuration also refers 
	 * to the given view, and uses the given file input source factory to load files
	 * in source directories.
	 * 
	 * The project configuration attaches itself to the given view to update its field
	 * when the project or the view are changed. The name of the project and the document loaders 
	 * of the view are synchronized.
	 * 
	 * @param projectName The name of the project being initialized.
	 * @param root The root directory of the project being initialized.
	 * @param view The view of the project being initialized.
	 * @param factory The file input source factory used to read source files.
	 */
 /*@
   @ public behavior
   @
   @ pre projectName != null;
   @ pre root != null;
   @ pre view != null;
   @ pre factory != null;
   @
   @ post name() == name;
   @ A new project is created.
   @ post \fresh(project());
   @ post project().root() == root;
   @ post project().views().contains(view);
   @ post view() == view;
   @ post fileInputSourceFactory = factory;
   @*/
	public ProjectConfiguration(String projectName, File root, View view, FileInputSourceFactory factory) {
		_view = view;
		_factory = factory;
		_name = projectName;
    setModelElement(new chameleon.workspace.Project(projectName, view, root));
    //FIXME fix this code when multi view support is added.
    //      I'd rather not do it before I know what I'm doing.
    project().addProjectListener(new ProjectListener() {
    	@Override
    	public void nameChanged(String name) {
    		_name = name;
    	}
    });
    
    // FIXME also called during input!
    view.addListener(new ViewListener() {
    	@Override
    	public void sourceLoaderAdded(DocumentLoader loader) {
    		ProjectConfiguration.this.sourceLoaderAdded(loader);
    	}
    	@Override
    	public void sourceLoaderRemoved(DocumentLoader loader) {
    		ProjectConfiguration.this.sourceLoaderRemoved(loader);
    	}
    	@Override
    	public void binaryLoaderAdded(DocumentLoader loader) {
    		ProjectConfiguration.this.binaryLoaderAdded(loader);
    	}
			@Override
    	public void binaryLoaderRemoved(DocumentLoader loader) {
				ProjectConfiguration.this.binaryLoaderRemoved(loader);
    	}
    });
	}
	
	/**
	 * This method overrides the default behavior and returns "project" 
	 * because this class does not have the same name as
	 * its corresponding XML element.
	 */
	@Override
	public String nodeName() {
		return "project";
	}
	
	/**
	 * Adds a node named "language" that contains the name and the version
	 * of the language of the view of this project configuration.
	 */
	protected void addImplicitChildren(Element result, Document doc) {
		Element lang = doc.createElement("language");
		lang.setAttribute("name", view().language().name());
		lang.setAttribute("version", view().language().version().toString());
		result.appendChild(lang);
	}

	/**
	 * Set the root directory of the project.
	 * 
	 * @param rootDirectory The new root directory of the project of this
	 *                      project configuration.
	 */
 /*@
   @ public behavior
   @
   @ pre rootDirectory != null;
   @ post project().root() == rootDirectory;
   @*/
	public void setRoot(File rootDirectory) {
		((Project)modelElement()).setRoot(rootDirectory);
	}
	
	/**
	 * Return the language of the project.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post result == view().language();
   @*/
	public Language language() {
		return ((Project)modelElement()).views().get(0).language();
	}
	
	protected void sourceLoaderAdded(DocumentLoader loader) {
		SourcePath p = createOrGetChild(SourcePath.class);
		p.createOrUpdateChild(SourcePath.Source.class, loader);
	}

	protected void sourceLoaderRemoved(DocumentLoader loader) {
		SourcePath p = createOrGetChild(SourcePath.class);
		p.removeChildFor(loader);
	}

	protected void binaryLoaderAdded(DocumentLoader loader) {
		BinaryPath p = createOrGetChild(BinaryPath.class);
		p.createOrUpdateChild(BinaryPath.Source.class,loader);
	}

	protected void binaryLoaderRemoved(DocumentLoader loader) {
		BinaryPath p = createOrGetChild(BinaryPath.class);
		p.removeChildFor(loader);
	}
	
	private String _name;
	
	/**
	 * Return the name of the project.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String name() {
		return _name;
	}
	
	/**
	 * Set the name of the project.
	 * 
	 * @param name The new name of the project.
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @ post name() == name;
   @ post project().name() == name;
   @*/
	public void setName(String name) {
		_name = name;
		project().setName(name);
	}
	
	private FileInputSourceFactory _factory;
	
	/**
	 * Return the file input source factory used to load source files of this
	 * project.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	protected FileInputSourceFactory fileInputSourceFactory() {
		return _factory;
	}
	
	private View _view;
	
	/**
	 * Return the view of this project.
	 * @return
	 */
	public View view() {
		return _view;
	}
	
	/**
	 * Return the project.
	 */
 /*@
   @ public behavior
   @
   @ post \result == modelElement();
   @*/
	public Project project() {
		return (Project) modelElement();
	}
	
	/**
	 * A node to configure the source path of a project.
	 */
	public class SourcePath extends ConfigElement {

		/**
		 * A node to configure a particular source directory.
		 */
		public class Source extends ProjectConfiguration.Source {

			/**
			 * After configuration, a {@link DirectoryLoader} is added to
			 * the project for the directory.
			 */
			protected void $after() throws ConfigException {
				try {
					DirectoryLoader loader = createLoader(fileInputSourceFactory());
					view().addSource(loader);
					setModelElement(loader);
				} catch (ProjectException e) {
					throw new ConfigException(e);
				}
			}
		}

		@Override
		protected void $update() {
			throw new ChameleonProgrammerException("A source path has no model element.");
		}
	}
	
	public class BinaryPath extends ConfigElement {
		
		@Override
		protected void $update() {
			throw new ChameleonProgrammerException("A source path has no model element.");
		}

		public class Source extends ProjectConfiguration.Source {
			protected void $after() throws ConfigException {
				try {
					view().addBinary(createLoader(fileInputSourceFactory()));
				} catch (ProjectException e) {
					throw new ConfigException(e);
				}
			}
			
		}
		
	}
	
	protected File file(String path) {
		File root = new File(path);
		if(!root.isAbsolute()) {
			root = new File(project().root().getAbsolutePath()+File.separator+path);
		}
		return root;
	}
	
	
	public abstract class Source extends ConfigElement {
		protected String _path;
		
		public void setRoot(String path) {
			_path = path;
		}
		
		public String getRoot() {
			return _path;
		}
		
		@Override
		protected void $update() {
			DirectoryLoader directoryLoader = (DirectoryLoader)modelElement();
			_path = directoryLoader.path();
//			_extension = directoryLoader.fileExtension();
		}

		protected DirectoryLoader createLoader(FileInputSourceFactory factory) {
			DirectoryLoader loader = new DirectoryLoader(_path, factory);
			for(String ext: sourceExtensions()) {
				loader.addFileExtension(ext);
			}
			return loader;
		}
	}
	
	//FIXME Move this to the ProjectConfigurator?
	protected abstract List<String> sourceExtensions();

	public void addSource(String path) {
		SourcePath sourcePath = createOrGetChild(SourcePath.class);
		SourcePath.Source element = sourcePath.createOrGetChild(chameleon.workspace.ProjectConfiguration.SourcePath.Source.class);
		element.setRoot(path);
	}

	@Override
	protected void $update() {
		_name = ((Project)modelElement()).name();
	}
}