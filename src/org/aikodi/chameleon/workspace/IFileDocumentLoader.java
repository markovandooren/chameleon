package org.aikodi.chameleon.workspace;

import java.io.File;

/**
 * An interface for document loaders that load documents from a file.
 * 
 * @author Marko van Dooren
 */
public interface IFileDocumentLoader extends DocumentLoader {

   /**
    * @return The file from which documents are loaded.
    */
	public File file();
}
