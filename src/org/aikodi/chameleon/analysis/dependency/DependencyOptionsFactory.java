package org.aikodi.chameleon.analysis.dependency;

import org.aikodi.chameleon.analysis.AnalysisOptions;
import org.aikodi.chameleon.plugin.LanguagePlugin;

public interface DependencyOptionsFactory extends LanguagePlugin {

	/**
	 * Create a default dependency analysis configuration for the language.
	 */
	public AnalysisOptions createConfiguration();
}
