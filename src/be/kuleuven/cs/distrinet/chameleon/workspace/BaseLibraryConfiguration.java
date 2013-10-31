package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;

public class BaseLibraryConfiguration {

	public BaseLibraryConfiguration(Workspace workspace) {
		setWorkspace(workspace);
	}
	
	private void setWorkspace(Workspace workspace) {
	if(workspace == null) {
		throw new IllegalArgumentException("The workspace of a base library configuration should not be null.");
	}
	_workspace = workspace;
	}
	
	private Workspace _workspace;
	
	public Workspace workspace() {
	return _workspace;
	}

  private Map<String, Boolean> _baseLibraryMap = new HashMap<>();
  
  
  public boolean mustLoad(be.kuleuven.cs.distrinet.chameleon.core.language.Language language) {
  	return mustLoad(language.name());
  }
  
  public boolean mustLoad(String language) {
  	Boolean tmp = _baseLibraryMap.get(language.toLowerCase());
  	return tmp == null ? true : tmp;
  }
  
  public void put(String languageName, boolean load) {
		_baseLibraryMap.put(languageName.toLowerCase(), load);
  }
}