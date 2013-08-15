package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;


public interface FileLoader extends DocumentLoader {

	public void tryToAdd(File file) throws InputException;

	public void tryToRemove(File file) throws InputException;

	public Document documentOf(File absoluteFile) throws InputException;
	
}
