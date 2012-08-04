package chameleon.workspace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.MultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.lookup.LookupException;
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
   @ post name() == name;
   @ post defaultNamespace() == defaultNamespace;
   @*/
	public Project(String name, RootNamespace defaultNamespace) {
		super(defaultNamespace);
	}

	private SingleAssociation<Project, Workspace> _workspaceLink;
	
	SingleAssociation<Project, Workspace> workspaceLink() {
		return _workspaceLink;
	}
	
	public Workspace workspace() {
		return _workspaceLink.getOtherEnd();
	}
	
	public void setWorkspace(Workspace workspace) {
		if(workspace != null) {
			workspace.add(this);
		} else {
			_workspaceLink.connectTo(null);
		}
	}
	
	public String name() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private String _name;

	private MultiAssociation<Project, ProjectReference> _projectDependencies;
	
	public List<ProjectReference> dependencyReferences() {
		return _projectDependencies.getOtherEnds();
	}
	
	public void add(ProjectReference projectReference) {
		_projectDependencies.add(projectReference.projectLink());
	}
	
	public void remove(ProjectReference projectReference) {
		_projectDependencies.remove(projectReference.projectLink());
	}

	public List<Project> dependencies() throws LookupException {
		List<Project> result = new ArrayList<Project>();
		for(ProjectReference ref: dependencyReferences()) {
			result.add(ref.getElement());
		}
		return result;
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
