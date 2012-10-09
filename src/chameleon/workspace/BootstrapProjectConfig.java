package chameleon.workspace;

import java.io.File;

import org.rejuse.junit.BasicRevision;
import org.rejuse.junit.Revision;
import org.w3c.dom.Element;

public class BootstrapProjectConfig extends ConfigElement {
	
	public BootstrapProjectConfig(File root, LanguageRepository repository) {
		_root = root;
		_repository = repository;
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
				_lang = _repository.get(_languageName, _revision); 
			} else {
				_lang = _repository.get(_languageName);
			}
		}
	}

	@Override
	protected void $after() throws ConfigException {
		ConfigElement pc = _lang.plugin(ConfigLoader.class).createConfigElement(_lang, _projectName, _root);
		for(Element element: unprocessedElements()) {
			pc.processChild(element);
		}
		_project = ((PConfig) pc).project();
	}
	
	public Project project() {
		return _project;
	}
	
	private Project _project;
}