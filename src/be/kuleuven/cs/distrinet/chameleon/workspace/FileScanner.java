package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;


public interface FileScanner extends DocumentScanner {

	public IFileInputSource tryToAdd(File file) throws InputException;

	public void tryToRemove(File file) throws InputException;

	public Document documentOf(File absoluteFile) throws InputException;
	
}
