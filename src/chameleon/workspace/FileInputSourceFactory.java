package chameleon.workspace;

import java.io.File;

public interface FileInputSourceFactory {

	InputSource create(File file);
}
