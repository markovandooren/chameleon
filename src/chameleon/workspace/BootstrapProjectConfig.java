package chameleon.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rejuse.junit.BasicRevision;
import org.rejuse.junit.Revision;
import org.w3c.dom.Element;

public class BootstrapProjectConfig extends ConfigElement {
	
	public BootstrapProjectConfig(File root, Workspace workspace) {
		_root = root;
		_workspace = workspace;
	}
	
	private List<ProjectInitialisationListener> _listeners = new ArrayList<ProjectInitialisationListener>();
	
	public void addListener(ProjectInitialisationListener listener) {
		if(listener == null) {
			throw new IllegalArgumentException();
		}
		_listeners.add(listener);
	}
	
	public void removeListener(ProjectInitialisationListener listener) {
		_listeners.remove(listener);
	}
	
	private File _root;
	
	public File root() {
		return _root;
	}
	
	private String _projectName;
	
	public void setName(String text) {
		_projectName = text;
	}
	
	private Workspace _workspace;
	
	private chameleon.core.language.Language _lang;
	
//	private boolean _loadBaseLibrary = false;
//	
//	public class DisableBaseLibrary extends ConfigElement {
//
//		@Override
//		protected void $after() throws ConfigException {
//			_loadBaseLibrary = false;
//		}
//		@Override
//		protected void $update() {
//		}
//		
//	}
	
	private BaseLibraryConfiguration _configuration = new BaseLibraryConfiguration();
	
	public static class BaseLibraryConfiguration {
	
	  private Map<String, Boolean> _baseLibraryMap = new HashMap<String, Boolean>();
	  
	  public boolean mustLoad(String language) {
	  	Boolean tmp = _baseLibraryMap.get(language.toLowerCase());
	  	if(tmp == null) {
	  		return true;
	  	} else {
	  		return tmp;
	  	}
	  }
	  
	  public void put(String language, boolean load) {
	  	_baseLibraryMap.put(language.toLowerCase(), load);
	  }
	}
	
	
	public class BaseLibraries extends ConfigElement {
		public class Library extends ConfigElement {
			private String _language;
			public void setLanguage(String language) {
				_language = language;
			}
			
			private boolean _load;
			public void setLoad(String load) {
				_load = Boolean.parseBoolean(load);
			}
			@Override
			protected void $after() throws ConfigException {
				_configuration.put(_language, _load);
			}
		}
	}
	
	protected LanguageRepository languageRepository() {
		return _workspace.languageRepository();
	}
	
	public class Language extends ConfigElement {
		
		public void setName(String text) {
			_languageName = text;
		}
		
		private String _languageName;
		
		public void setVersion(String text) {
			_revision = new BasicRevision(text);
		}
		
		private Revision _revision;
		
		@Override
		protected void $after() throws ConfigException {
			if(_revision != null) {
				setModelElement(languageRepository().get(_languageName, _revision));
			} else {
				setModelElement(languageRepository().get(_languageName));
			}
			_lang = (chameleon.core.language.Language) modelElement();
		}

		@Override
		protected void $update() {
			chameleon.core.language.Language language = (chameleon.core.language.Language)modelElement();
			_languageName = language.name();
			_revision = language.version();
		}
	}

	@Override
	protected void $after() throws ConfigException {
		ConfigElement pc = _lang.plugin(ProjectConfigurator.class).createConfigElement(_projectName, _root, _workspace,_listener,_configuration);
		for(Element element: unprocessedElements()) {
			pc.processChild(element);
		}
		_project = ((ProjectConfiguration) pc).project();
	}
	
	private ProjectInitialisationListener _listener;
	
	public Project project(File xmlFile, ProjectInitialisationListener listener) throws ConfigException {
		_listener = listener;
		readFromXML(xmlFile);
		return _project;
	}
	
	private Project _project;

	@Override
	protected void $update() {
		// the bootstrapper should not be updated
	}
}