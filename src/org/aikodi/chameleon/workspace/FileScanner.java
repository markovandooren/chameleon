package org.aikodi.chameleon.workspace;

import java.io.File;

import org.aikodi.chameleon.core.document.Document;


public interface FileScanner extends DocumentScanner {

	public IFileDocumentLoader tryToAdd(File file) throws InputException;

	public void tryToRemove(File file) throws InputException;

	public Document documentOf(File absoluteFile) throws InputException;
	
}
