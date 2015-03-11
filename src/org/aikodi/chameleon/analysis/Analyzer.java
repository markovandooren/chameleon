package org.aikodi.chameleon.analysis;

import java.util.Collection;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;

public abstract class Analyzer {

	/**
	 * Create a new analyser for the given project.
	 * @param project
	 */
 /*@
   @ public behavior
   @
   @ pre project != null;
   @ post project() == project;
   @*/
	public Analyzer(Project project) {
		_project = project;
	}
	
	private Project _project;
	
	/**
	 * Return the result of performing the given analysis on the project of this
	 * analyser.
	 * @param analysis
	 * @return
	 * @throws InputException
	 */
	protected <R extends Result<R>> R analysisResult(Analysis<?,R> analysis) throws InputException {
		for(Document doc: sourceDocuments()) {
//			doc.apply(analysis);
		}
		return analysis.result();
	}

	public Project project() {
		return _project;
	}
	
	public Collection<Document> sourceDocuments() throws InputException {
		return project().sourceDocuments();
	}

}
