package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import be.kuleuven.cs.distrinet.rejuse.junit.BasicRevision;
import be.kuleuven.cs.distrinet.rejuse.junit.Revision;

public class BootstrapProjectConfig extends ConfigElement {
	
	public BootstrapProjectConfig(Workspace workspace) {
//		_root = root;
		_workspace = workspace;
		_configuration = new BaseLibraryConfiguration(workspace);
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
	
//	private File _root;
//	
//	public File root() {
//		return _root;
//	}
	
	private String _projectName;
	
	public void setName(String text) {
		_projectName = text;
	}
	
	private Workspace _workspace;
	
	private be.kuleuven.cs.distrinet.chameleon.core.language.Language _lang;
	
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
	
	private final BaseLibraryConfiguration _configuration;
	
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
			_lang = (be.kuleuven.cs.distrinet.chameleon.core.language.Language) modelElement();
		}

		@Override
		protected void $update() {
			be.kuleuven.cs.distrinet.chameleon.core.language.Language language = (be.kuleuven.cs.distrinet.chameleon.core.language.Language)modelElement();
			_languageName = language.name();
			_revision = language.version();
		}
	}

	
	private void myafter(File root) throws ConfigException {
		ConfigElement pc = _lang.plugin(ProjectConfigurator.class).createConfigElement(_projectName, root, _workspace,_listener,_configuration);
		for(Element element: unprocessedElements()) {
			pc.processChild(element);
		}
		_project = ((ProjectConfiguration) pc).project();
	}
	
	private ProjectInitialisationListener _listener;
	
	public Project project(File xmlFile, ProjectInitialisationListener listener) throws ConfigException {
		_listener = listener;
		readFromXML(xmlFile);
		myafter(xmlFile.getParentFile());
		return _project;
	}
	
	private Project _project;

	@Override
	protected void $update() {
		// the bootstrapper should not be updated
	}
}
