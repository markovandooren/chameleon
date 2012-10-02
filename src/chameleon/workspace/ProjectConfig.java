package chameleon.workspace;

import java.io.File;

import chameleon.core.language.LanguageFactory;
import chameleon.core.namespace.LazyRootNamespace;

public class ProjectConfig extends ConfigElement {
	
	public ProjectConfig(File root, LanguageFactory factory, FileInputSourceFactory inputSourceFactory) {
		_view = new View(new LazyRootNamespace(), factory.create());
		setProject(new chameleon.workspace.Project(null, _view, root));
		_root = root;
		_inputSourceFactory = inputSourceFactory;
	}
	
	private View _view;
	
	public View view() {
		return _view;
	}
	
	private File _root;
	
	public File root() {
		return _root;
	}
	
	private chameleon.workspace.Project _project;
	
	public chameleon.workspace.Project project() {
		return _project;
	}
	
	protected void setProject(chameleon.workspace.Project project) {
		_project = project;
	}
	
	public void setName(String text) {
		project().setName(text);
	}
	
	public String getName() {
		return project().name();
	}
	
	public class Language extends ConfigElement {
	}
	
	protected FileInputSourceFactory fileInputSourceFactory() {
		return _inputSourceFactory;
	}
	
	private FileInputSourceFactory _inputSourceFactory;

	public class SourcePath extends ConfigElement {
		public class Source extends ProjectConfig.Source {
			protected void $after() throws ConfigException {
				try {
					view().addSource(new DirectoryLoader(_extension, file(_path), fileInputSourceFactory()));
				} catch (ProjectException e) {
					throw new ConfigException(e);
				}
			}

		}
	}
	
	
	public class BinaryPath extends ConfigElement {
		// Duplicate for now, but that will change when proper support for "binary" modules is added.
		public class Source extends ProjectConfig.Source {
			protected void $after() throws ConfigException {
				try {
					view().addBinary(new DirectoryLoader(_extension, file(_path), fileInputSourceFactory()));
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
	
	
	public static class Source extends ConfigElement {
		
		
		protected String _path;
		
		public void setRoot(String path) {
			_path = path;
		}
		
		protected String _extension;
		
		public void setExtension(String text) {
			_extension = text;
		}
		
	}
	
}