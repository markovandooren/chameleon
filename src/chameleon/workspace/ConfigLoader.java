package chameleon.workspace;

import java.io.File;

import chameleon.core.language.Language;
import chameleon.plugin.Plugin;

public interface ConfigLoader extends Plugin {

	public ConfigElement createConfigElement(Language language, String projectName, File root) throws ConfigException;
}
