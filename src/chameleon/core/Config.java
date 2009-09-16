package chameleon.core;

public class Config {

	/**************************
	 * OPTIMISATION CONSTANTS *
	 **************************/
	
	public static boolean DEBUG=true;
	
	private static boolean CACHE_DECLARATIONS = false;

	/**
	 * Turn cache of element references on and off. Default is on.
	 */
	private static boolean CACHE_ELEMENT_REFERENCES = false;
	
	/**
	 * Turn cache of expression types on and off. Default is on.
	 */
	private static boolean CACHE_EXPRESSION_TYPES = false;

	/**
	 * Turn cache of language on and off. Default is on.
	 */
	private static boolean CACHE_LANGUAGE = false;

	public static void setCaching(boolean bool) {
		CACHE_DECLARATIONS = bool;
		CACHE_ELEMENT_REFERENCES = bool;
		CACHE_LANGUAGE = bool;
		CACHE_EXPRESSION_TYPES = bool;
	}

	public static boolean cacheExpressionTypes() {
		return CACHE_EXPRESSION_TYPES;
	}

	public static boolean cacheLanguage() {
		return CACHE_LANGUAGE;
	}

	public static boolean cacheElementReferences() {
		return CACHE_ELEMENT_REFERENCES;
	}

	public static boolean cacheDeclarations() {
		return CACHE_DECLARATIONS;
	}
}
