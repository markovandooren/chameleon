package chameleon.workspace;

import java.util.List;

import org.rejuse.association.AssociationListener;
import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

public abstract class ProjectLoaderImpl implements ProjectLoader {

	public ProjectLoaderImpl() {
		_projectLink.addListener(new AssociationListener<Project>() {

			// WARNING
			
			// WE TUNNEL THE EXCEPTION THROUGH THE ASSOCIATION CLASSES
			// AND PERFORM THE ROLLBACK IN {@link Project#addSource(ProjectLoader)}
			@Override
			public void notifyElementAdded(Project element) {
				try {
					notifyProjectAdded(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementRemoved(Project element) {
				try {
					notifyProjectRemoved(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementReplaced(Project oldElement, Project newElement) {
				try {
					notifyProjectReplaced(oldElement, newElement);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}
		});
	}
	
	static class TunnelException extends RuntimeException {

		public TunnelException(Throwable cause) {
			super(cause);
		}
		
	}
	
	protected void notifyProjectAdded(Project project) throws ProjectException {
		
	}
	
	protected void notifyProjectRemoved(Project project) throws ProjectException {
		
	}

	protected void notifyProjectReplaced(Project old, Project newProject) throws ProjectException {
		
	}


	
	private SingleAssociation<ProjectLoaderImpl, Project> _projectLink = new SingleAssociation<ProjectLoaderImpl, Project>(this);
	
	public SingleAssociation<ProjectLoaderImpl, Project> projectLink() {
		return _projectLink;
	}

	public Project project() {
		return _projectLink.getOtherEnd();
	}

	private OrderedMultiAssociation<ProjectLoaderImpl, InputSource> _inputSources = new OrderedMultiAssociation<ProjectLoaderImpl, InputSource>(this);
	
	public void addInputSource(InputSource source) {
		if(source != null) {
			_inputSources.add(source.loaderLink());
		}
	}

	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}
	
	public void removeInputSource(InputSource source) {
		if(source != null) {
			_inputSources.remove(source.loaderLink());
		}
	}
}
