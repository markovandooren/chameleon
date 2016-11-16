package org.aikodi.chameleon.workspace;

import java.util.Collection;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.association.MultiAssociation;

/**
 * A workspace holds a number of projects, and a reference to a repository
 * with supported languages.
 * 
 * @author Marko van Dooren
 */
public class Workspace {

  /**
   * Create a new workspace with the given language repository.
   * 
   * @param repository A repository containing the supported languages.
   */
  public Workspace(LanguageRepository repository) {
		_repository = repository;
	}
	
	private MultiAssociation<Workspace, Project> _projects = new MultiAssociation<Workspace, Project>(this);
	
	/**
	 * @return the projects in the workspace. All references in the resulting
	 * collection point to different projects.
	 */
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
			if(project.getName().equals(name)) {
				return project;
			}
		}
		throw new LookupException("Project with name "+name+" could not be found.");
	}
}
