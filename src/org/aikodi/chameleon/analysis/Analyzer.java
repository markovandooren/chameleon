package org.aikodi.chameleon.analysis;

import java.util.Collection;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.action.TopDown;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.Project;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

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
	 * Perform the given analysis on the project of this analyzer.
	 * 
	 * @param analysis The analysis to be executed.
	 * @return The result of performing the given analysis top down on every
   * source document in the project. 
	 * @throws InputException
	 */
	protected <R extends Result<R>> R analysisResult(Analysis<? extends Element,R> analysis) throws InputException {
		for(Document doc: sourceDocuments()) {
			TreeStructure<Element> lexical = doc.lexical();
      new TopDown<Element,Nothing>(analysis).traverse(lexical);
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
