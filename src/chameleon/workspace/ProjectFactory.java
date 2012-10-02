package chameleon.workspace;

import java.io.File;

public abstract class ProjectFactory {

	/**
	 * Create a new project using the configuration in the given file.
	 * @param xmlFile
	 * @return
	 * @throws ConfigException
	 */
	public abstract Project createProject(File xmlFile) throws ConfigException;
}
