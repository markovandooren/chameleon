package chameleon.workspace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.AssociationListener;
import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

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
		_views.addListener(new AssociationListener<View>() {

			@Override
			public void notifyElementAdded(View element) {
				notifyViewAdded(element);
			}

			@Override
			public void notifyElementRemoved(View element) {
				notifyViewRemoved(element);
			}
		});
	}
	
	protected void notifyViewAdded(View view) {
		for(ProjectListener listener: _listeners) {
			listener.viewAdded(view);
		}
	}
	
	protected void notifyViewRemoved(View view) {
		for(ProjectListener listener: _listeners) {
			listener.viewRemoved(view);
		}
	}
	
	public void addProjectListener(ProjectListener listener) {
		if(listener == null) {
			throw new IllegalArgumentException();
		}
		_listeners.add(listener);
	}
	
	public void removeProjectListener(ProjectListener listener) {
		_listeners.remove(listener);
	}
	
	
	private List<ProjectListener> _listeners = new ArrayList<>();
	
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
		if(file != null && file.isFile()) {
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

	/**
	 * Try to add the given file to the project. Each FileLoader in each view
	 * will be given the opportunity to added the file.
	 * 
	 * FIXME: Can we avoid letting the Project know about files and FileLoader?
	 * @param file
	 */
	public void tryToAdd(File file) {
		for(View view: views()) {
			for(FileLoader loader: view.sourceLoaders(FileLoader.class)) {
				try {
					loader.tryToAdd(file);
				} catch (InputException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		flushSourceCache();
	}
	
	public void tryToRemove(File file) {
		for(View view: views()) {
			for(FileLoader loader: view.sourceLoaders(FileLoader.class)) {
				try {
					loader.tryToRemove(file);
				} catch (InputException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		flushSourceCache();
	}
	
	/**
	 * Flush all caches in the sources of this project. Each
	 * view is sent a message to flush its caches.
	 */
	public void flushSourceCache() {
		for(View view: views()) {
			view.flushSourceCache();
		}
	}
}
