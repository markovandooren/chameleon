package chameleon.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rejuse.junit.BasicRevision;
import org.rejuse.junit.Revision;
import org.w3c.dom.Element;

public class BootstrapProjectConfig extends ConfigElement {
	
	public BootstrapProjectConfig(File root, LanguageRepository repository) {
		_root = root;
		_repository = repository;
	}
	
	private List<ProjectInitialisationListener> _listeners = new ArrayList<>();
	
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
	
	private LanguageRepository _repository;
	
	private chameleon.core.language.Language _lang;
	
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
				setModelElement(_repository.get(_languageName, _revision));
			} else {
				setModelElement(_repository.get(_languageName));
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
		ConfigElement pc = _lang.plugin(ProjectConfigurator.class).createConfigElement(_projectName, _root, _listener);
		for(Element element: unprocessedElements()) {
			pc.processChild(element);
		}
		_project = ((ProjectConfig) pc).project();
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