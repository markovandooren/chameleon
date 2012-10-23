package chameleon.workspace;

import java.io.File;


public interface FileLoader extends DocumentLoader {

	public void tryToAdd(File file) throws InputException;

	public void tryToRemove(File file) throws InputException;
	
}
