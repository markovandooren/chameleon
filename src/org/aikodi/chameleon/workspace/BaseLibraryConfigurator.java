package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.language.Language;

/**
 * A class for configuring adding the base library of a language to a view.
 * Depending on how a Chameleon module is packaged, the base library may
 * have to be loaded in a different way. Different base library configurators
 * can be used to accomodate this variability.
 * 
 * A base library configurator 
 * 
 * @author Marko van Dooren
 */
public abstract class BaseLibraryConfigurator {

	/**
	 * Create a new base library configurator.
	 */
	public BaseLibraryConfigurator(Language language) {
		if(language == null) {
			throw new IllegalArgumentException("The given language is null.");
		}
		_language = language;
	}
	
	/**
	 * Add the base library of the language managed by this base library configurator
	 * if that is indicated by the base library configuration.
	 * @param view
	 * @param configuration
	 */
	public void process(View view, BaseLibraryConfiguration configuration) {
		if (next() != null) {
			next().process(view, configuration);
		}
		if (configuration.mustLoad(languageName())) {
			addBaseScanner(view);
		}
	}

	/**
	 * The name of the language for which the configurator will configure the base library.
	 * @return
	 */
	protected abstract String languageName();
	
	/**
	 * Return the next base library configurator in the chain. 
	 * Returns null if no such base library configurator exists.
	 */
	public BaseLibraryConfigurator next() {
		return _next;
	}
	
	public void setNext(BaseLibraryConfigurator next) {
		if(next != null && next.hasDescendant(this)) {
			throw new IllegalArgumentException("About the create a loop in the base library configurator chain.");
		}
		_next = next;
	}
	
	protected boolean hasDescendant(BaseLibraryConfigurator configurator) {
		return _next == configurator || (_next != null && _next.hasDescendant(configurator));
	}
	
	private BaseLibraryConfigurator _next;
	
	protected abstract void addBaseScanner(View view);

	/**
	 * Return the language object for which this base library configurator
	 * can add the base library. 
	 */
	public final Language language() {
		return _language;
	}
	
	private Language _language;
}
