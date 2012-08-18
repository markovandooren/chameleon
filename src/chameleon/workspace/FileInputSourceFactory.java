package chameleon.workspace;

import java.io.File;
import java.io.IOException;

import chameleon.input.ParseException;

public interface FileInputSourceFactory {

	InputSource create(File file) throws IOException, ParseException;
	
	void popDirectory();
	
	void pushDirectory(String name);
}
