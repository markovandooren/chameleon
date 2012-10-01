package chameleon.workspace;

import java.util.List;

import org.rejuse.association.MultiAssociation;

import chameleon.core.lookup.LookupException;

public class Workspace {

	private MultiAssociation<Workspace, Project> _projects;
	
	public List<Project> projects() {
		return _projects.getOtherEnds();
	}
	
	public void add(Project project) {
		_projects.add(project.workspaceLink());
	}
	
	public void remove(Project project) {
		_projects.remove(project.workspaceLink());
	}

	public Project getProject(String name) throws LookupException {
		for(Project project: projects()) {
			if(project.name().equals(name)) {
				return project;
			}
		}
		throw new LookupException("Project with name "+name+" could not be found.");
	}
}
