package be.kuleuven.cs.distrinet.chameleon.analysis;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;

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
			doc.apply(analysis);
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
