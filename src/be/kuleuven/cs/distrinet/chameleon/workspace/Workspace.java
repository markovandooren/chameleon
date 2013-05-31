package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.Collection;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.rejuse.association.MultiAssociation;

public class Workspace {

	public Workspace(LanguageRepository repository) {
		_repository = repository;
	}
	
	private MultiAssociation<Workspace, Project> _projects = new MultiAssociation<Workspace, Project>(this);
	
	public Collection<Project> projects() {
		return _projects.getOtherEnds();
	}
	
	public void add(Project project) {
		_projects.add(project.workspaceLink());
	}
	
	public void remove(Project project) {
		_projects.remove(project.workspaceLink());
	}
	
	public LanguageRepository languageRepository() {
		return _repository;
	}
	
	private LanguageRepository _repository;

	public Project getProject(String name) throws LookupException {
		for(Project project: projects()) {
			if(project.name().equals(name)) {
				return project;
			}
		}
		throw new LookupException("Project with name "+name+" could not be found.");
	}
}
