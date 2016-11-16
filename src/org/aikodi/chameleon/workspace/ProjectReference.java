package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.association.SingleAssociation;

public class ProjectReference {

	public String name() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	private String _name;

	
	private SingleAssociation<ProjectReference, Project> _projectLink;
	
	SingleAssociation<ProjectReference, Project> projectLink() {
		return _projectLink;
	}
	
	public Project project() {
		return _projectLink.getOtherEnd();
	}

	
	public Project getElement() throws LookupException {
		Workspace ws = workspace();
		return ws.getProject(name());
	}

	public Workspace workspace() {
		return project().workspace();
	}
}
