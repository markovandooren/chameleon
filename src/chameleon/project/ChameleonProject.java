package chameleon.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chameleon.core.namespace.RootNamespace;
import chameleon.input.ParseException;

/**
 * A class that represents the concept of a project. A project
 * keeps a collection of input sources and is an input source itself.
 * 
 * 
 * @author Marko van Dooren
 * @author Nelis Boucke
 */
public abstract class ChameleonProject extends InputSource {
	
	public ChameleonProject(RootNamespace defaultNamespace) {
		super(defaultNamespace);
	}

	private List<InputSource> _inputSources;

	public List<InputSource> inputSources() {
		return new ArrayList<InputSource>(_inputSources);
	}

	/**
	 * 
	 * @param input
	 */
	public void add(InputSource input) {
		_inputSources.add(input);
	}
	
	public void remove(InputSource input) {
		_inputSources.remove(input);
	}

	/**
	 * Refresh the project. This performs a refresh on all 
	 * input sources.
	 */
	@Override
	public void refresh() throws ParseException, IOException {
		for(InputSource input: _inputSources) {
			input.refresh();
		}
	}
	
	

}
