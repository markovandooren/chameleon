package be.kuleuven.cs.distrinet.chameleon.analysis.dependency;

import be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency.DependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePlugin;

public interface DependencyOptionsFactory extends LanguagePlugin {

	/**
	 * Create a default dependency analysis configuration for the language.
	 */
	public DependencyOptions createConfiguration();
}
