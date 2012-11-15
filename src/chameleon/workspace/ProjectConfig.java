package chameleon.workspace;

import java.io.File;

import chameleon.core.language.Language;
import chameleon.exception.ChameleonProgrammerException;

/**
 * A ProjectConfig mirrors the configuration of a Chameleon project. To load a project,
 * a {@link BootstrapProjectConfig} object is created. The bootstrapper is given the root
 * directory of the project (the directory in which project.xml is found), and a {@link LanguageRepository}.
 * The bootstrapper reads the language from the XML file by reading the XML nodes, and looks up the corresponding {@link Language} object
 * in the language repository. The bootstrapper gets the {@link ConfigLoader} plugin via {@link Language#plugin(Class)} and
 * invokes {@link ConfigLoader#createConfigElement(String, File, ProjectInitialisationListener)} to create an object of 
 * the appropriate subclass of ProjectConfig. In the last step, the bootstrapper tells the project config to process the
 * non-language nodes in the configuration file (only the language node is used by the bootstrapper itself).
 *  
 * @author Marko van Dooren
 */
public class ProjectConfig extends ConfigElement {

	@Override
	public String nodeName() {
		return "project";
	}
	
	public ProjectConfig(View view, FileInputSourceFactory factory, String projectName, File root) {
		_view = view;
		_factory = factory;
		_name = projectName;
    setModelElement(new chameleon.workspace.Project(projectName, view, root));
    //FIXME activate and fix this code when multi view support is added.
    //      I'd rather not do it before I know what I'm doing.
    project().addProjectListener(new ProjectListener() {
    	@Override
    	public void viewAdded(View view) {
    		
    	}
    	
    	@Override
    	public void viewRemoved(View view) {
    		
    	}
    	
    	@Override
    	public void nameChanged(String name) {
    		_name = name;
    	}
    });
    
    
    // FIXME also called during input!
    view.addListener(new ViewListener() {
    	@Override
    	public void sourceLoaderAdded(DocumentLoader loader) {
    		ProjectConfig.this.sourceLoaderAdded(loader);
    	}
    	@Override
    	public void sourceLoaderRemoved(DocumentLoader loader) {
    		ProjectConfig.this.sourceLoaderRemoved(loader);
    	}
    	@Override
    	public void binaryLoaderAdded(DocumentLoader loader) {
    		ProjectConfig.this.binaryLoaderAdded(loader);
    	}
			@Override
    	public void binaryLoaderRemoved(DocumentLoader loader) {
				ProjectConfig.this.binaryLoaderRemoved(loader);
    	}
    });
	}
	
	public void setRoot(File rootDirectory) {
		((Project)modelElement()).setRoot(rootDirectory);
	}
	
	public Language language() {
		return ((Project)modelElement()).views().get(0).language();
	}
	
	protected void sourceLoaderAdded(DocumentLoader loader) {
		SourcePath p = createOrGetChild(SourcePath.class);
//		p.$createChild(loader);
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
	
	public String name() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private FileInputSourceFactory _factory;
	
	protected FileInputSourceFactory fileInputSourceFactory() {
		return _factory;
	}
	
	private View _view;
	
	public View view() {
		return _view;
	}
	
	public Project project() {
		return (Project) modelElement();
	}
	
	public class SourcePath extends ConfigElement {
		
		public class Source extends ProjectConfig.Source {
			
			protected void $after() throws ConfigException {
				try {
					DirectoryLoader loader = new DirectoryLoader(_extension, _path, fileInputSourceFactory());
					view().addSource(loader);
					setModelElement(loader);
				} catch (ProjectException e) {
					throw new ConfigException(e);
				}
			}

		}

//		public void $createChild(DocumentLoader loader) {
//			Source s = new Source();
//			addChild(s);
//			s.$synch(loader);
//		}

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

		public class Source extends ProjectConfig.Source {
			protected void $after() throws ConfigException {
				try {
					view().addBinary(new DirectoryLoader(_extension, _path, fileInputSourceFactory()));
				} catch (ProjectException e) {
					throw new ConfigException(e);
				}
			}
			
		}
//		public void $createChild(DocumentLoader loader) {
//			Source s = new Source();
//			addChild(s);
//			s.$synch(loader);
//		}
		
	}
	
	protected File file(String path) {
		File root = new File(path);
		if(!root.isAbsolute()) {
			root = new File(project().root().getAbsolutePath()+File.separator+path);
		}
		return root;
	}
	
	
	public static class Source extends ConfigElement {
		protected String _path;
		
		public void setRoot(String path) {
			_path = path;
		}
		
		protected String _extension;
		
		public void setExtension(String text) {
			_extension = text;
		}

		@Override
		protected void $update() {
			DirectoryLoader directoryLoader = (DirectoryLoader)modelElement();
			_path = directoryLoader.path();
			_extension = directoryLoader.fileExtension();
		}

	}
	
	public void addSource(String path) {
		SourcePath sourcePath = createOrGetChild(SourcePath.class);
		SourcePath.Source element = sourcePath.new Source();
		element.setRoot(path);
		addChild(element);
	}

	@Override
	protected void $update() {
		_name = ((Project)modelElement()).name();
	}
}