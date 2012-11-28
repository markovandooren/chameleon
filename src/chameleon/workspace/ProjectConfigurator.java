package chameleon.workspace;

import java.io.File;

import chameleon.plugin.LanguagePlugin;

public interface ProjectConfigurator extends LanguagePlugin {

	public ProjectConfig createConfigElement(String projectName, File root, ProjectInitialisationListener listener) throws ConfigException;
}
