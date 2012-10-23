package chameleon.workspace;

import java.io.File;

public class ProjectConfig extends ConfigElement {

	public ProjectConfig(View view, FileInputSourceFactory factory, String projectName, File root) {
		_view = view;
		_factory = factory;
    setProject(new chameleon.workspace.Project(projectName, view, root));
	}
	
	private FileInputSourceFactory _factory;
	
	protected FileInputSourceFactory fileInputSourceFactory() {
		return _factory;
	}
	
	private View _view;
	
	public View view() {
		return _view;
	}
	
	private chameleon.workspace.Project _project;
	
	public chameleon.workspace.Project project() {
		return _project;
	}
	
	protected void setProject(chameleon.workspace.Project project) {
		_project = project;
	}
	
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
