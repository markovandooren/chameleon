package chameleon.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Association;
import org.rejuse.association.MultiAssociation;
import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.RootNamespace;
import chameleon.workspace.DocumentLoaderImpl.TunnelException;

/**
 * A class that represents the concept of a project. A project
 * keeps a collection of input sources and is an input source itself.
 * 
 * @author Marko van Dooren
 */
public class Project {
	
	/**
	 * Create a new Chameleon project for the given default namespace.
	 */
 /*@
   @ public behavior
   @
   @ pre name != null;
   @ pre root != null;
   @
   @ post name() == name;
   @ post namespace() == root;
   @*/
	public Project(String name, View view, File root) {
		setName(name);
		addView(view);
		setRoot(root);
	}
	
	public void addView(View view) {
		if(view != null) {
			_views.add(view.projectLink());
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public List<View> views() {
		return _views.getOtherEnds();
	}
	
	private OrderedMultiAssociation<Project, View> _views = new OrderedMultiAssociation<Project, View>(this);

	private SingleAssociation<Project, Workspace> _workspaceLink;
	
	public File root() {
		return _root;
	}
	
	private File _root;
	
	protected void setRoot(File file) {
		if(file == null) {
			throw new IllegalArgumentException("The root directory of a project should not be null");
		}
		if(file.isFile()) {
			throw new IllegalArgumentException("The root directory of a project should not be a normal file");
		}
		_root = file;
	}
	
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

//	private MultiAssociation<Project, ProjectReference> _projectDependencies;
//	
//	public List<ProjectReference> dependencyReferences() {
//		return _projectDependencies.getOtherEnds();
//	}
//	
//	public void add(ProjectReference projectReference) {
//		_projectDependencies.add(projectReference.projectLink());
//	}
//	
//	public void remove(ProjectReference projectReference) {
//		_projectDependencies.remove(projectReference.projectLink());
//	}

//	public List<Project> dependencies() throws LookupException {
//		List<Project> result = new ArrayList<Project>();
//		for(ProjectReference ref: dependencyReferences()) {
//			result.add(ref.getElement());
//		}
//		return result;
//	}
	


//	/**
//	 * Refresh the project. This performs a refresh on all 
//	 * input sources.
//	 */
//	public void refresh() throws ParseException, IOException {
//		for(InputSource input: _inputSources) {
//			input.refresh();
//		}
//	}
}
