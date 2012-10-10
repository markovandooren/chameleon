package chameleon.workspace;

import java.io.File;

import chameleon.core.language.Language;
import chameleon.plugin.LanguagePlugin;

public interface ConfigLoader extends LanguagePlugin {

	public ConfigElement createConfigElement(Language language, String projectName, File root) throws ConfigException;
}
