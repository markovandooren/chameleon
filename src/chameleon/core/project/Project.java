package chameleon.core.project;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import chameleon.core.namespace.RootNamespace;
import chameleon.input.ParseException;

/**
 * A class that represents the concept of a project. A project
 * keeps a collection of input sources and is an input source itself.
 * 
 * @author Marko van Dooren
 * @author Nelis Boucke
 */
public abstract class Project extends InputSource {
	
	/**
	 * Create a new Chameleon project for the given default namespace.
	 */
 /*@
   @ public behavior
   @
   @ pre defaultNamespace != null;
   @
   @ post defaultNamespace() == defaultNamespace;
   @*/
	public Project(RootNamespace defaultNamespace) {
		super(defaultNamespace);
	}

	private Set<InputSource> _inputSources = new HashSet<InputSource>();

	/**
	 * Return the input sources for this project.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Set<InputSource> inputSources() {
		return new HashSet<InputSource>(_inputSources);
	}

	/**
	 * Add the given input source to this project.
	 */
 /*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post inputSources().contains(input);
   @*/
	public void add(InputSource input) {
		if(isValid(input)) {
		  _inputSources.add(input);
		}
	}
	
	/**
	 * Add the given input source to this project.
	 */
 /*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void remove(InputSource input) {
		_inputSources.remove(input);
	}
	
	public boolean isValid(InputSource input) {
		return input != null;
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
