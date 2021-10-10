package org.aikodi.chameleon.plugin.build;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.plugin.ViewPlugin;

import java.io.File;
import java.util.List;

/**
 * A builder translates documents to some output format.
 * 
 * @author Marko van Dooren
 */
public interface Builder extends ViewPlugin {

  /**
   * Build the given documents and write the output in the given
   * output directory.
   * 
   * FIXME: Get rid of the File dependency. It doesn't belong here.
   * 
   * @param documents The documents that will be built.
   * @param outputDir The directory in which the output must be written.
   * @param buildProgressHelper An object to indicate the progress of the build.
   * 
   * @throws BuildException An exception occurred while building.
   */
	public void build(List<Document> documents, File outputDir, BuildProgressHelper buildProgressHelper) throws BuildException;
	
	/**
	 * Return an indication for the total amount of work to be done when building
	 * the given collection of documents.
	 * 
	 * @param documents The documents that should be built.
	 * @param allDocuments The list of all documents.
	 * 
	 * @return A number that indicates how much work must be done to build the
	 * given collection of documents.
	 */
	public int totalAmountOfWork(List<Document> documents, List<Document> allDocuments);
}
