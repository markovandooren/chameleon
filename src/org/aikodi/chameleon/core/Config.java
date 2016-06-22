package org.aikodi.chameleon.core;

/**
 * FIXME This static piece of .... should go.
 * @author Marko van Dooren
 */
public class Config {

	/**************************
	 * OPTIMISATION CONSTANTS *
	 **************************/
	
	private static boolean DEBUG=false;
	
	private static boolean CACHE_DECLARATIONS = true;

	/**
	 * Turn cache of element references on and off. Default is off.
	 */
	private static boolean CACHE_ELEMENT_REFERENCES = true;
	
	/**
	 * Turn cache of properties on and off. Default is off.
	 */
	private static boolean CACHE_ELEMENT_PROPERTIES = true;
	
	/**
	 * Turn cache of expression types on and off. Default is off.
	 */
	private static boolean CACHE_EXPRESSION_TYPES = true;

	/**
	 * Turn cache of signatures on and off. Default is off.
	 */
	private static boolean CACHE_SIGNATURES = true;

	/**
	 * Turn cache of language on and off. Default is off.
	 */
	private static boolean CACHE_LANGUAGE = true;

	public static boolean debug() {
		return DEBUG;
	}
	
	public static void setCaching(boolean bool) {
		setCacheDeclarations(bool);
		setCacheElementReferences(bool);
		setCacheLanguage(bool);
		setCacheExpressionTypes(bool);
		setCacheElementProperties(bool);
		setCacheSignatures(bool);
	}

	public static boolean cacheSignatures() {
		return CACHE_SIGNATURES;
	}

	public static void setCacheSignatures(boolean value) {
		CACHE_SIGNATURES = value;
	}

	public static boolean cacheExpressionTypes() {
		return CACHE_EXPRESSION_TYPES;
	}

	public static void setCacheExpressionTypes(boolean value) {
		CACHE_EXPRESSION_TYPES = value;
	}

	public static boolean cacheElementProperties() {
		return CACHE_ELEMENT_PROPERTIES;
	}

	public static void setCacheElementProperties(boolean value) {
		CACHE_ELEMENT_PROPERTIES = value;
	}

	public static boolean cacheLanguage() {
		return CACHE_LANGUAGE;
	}

	public static void setCacheLanguage(boolean value) {
		CACHE_LANGUAGE = value;
	}

	public static boolean cacheElementReferences() {
		return CACHE_ELEMENT_REFERENCES;
	}
	
	public static void setCacheElementReferences(boolean value) {
		CACHE_ELEMENT_REFERENCES = value;
	}

	public static boolean cacheDeclarations() {
		return CACHE_DECLARATIONS;
	}

	public static void setCacheDeclarations(boolean value) {
		CACHE_DECLARATIONS = value;
	}
	
	public static boolean singleThreaded() {
		return SINGLE_THREADED;
	}
	
	public static void setSingleThreaded(boolean value) {
		SINGLE_THREADED = value;
	}
	
	private static boolean SINGLE_THREADED=true;


}
