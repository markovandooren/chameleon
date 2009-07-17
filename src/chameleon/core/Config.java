package chameleon.core;

public class Config {

	/**************************
	 * OPTIMISATION CONSTANTS *
	 **************************/
	
	public final static boolean DEBUG=true;
	
	public final static boolean CACHE_ALL_TYPE_NAMES = true;
	
	public static boolean CACHE_DECLARATIONS = true;

	/**
	 * Turn cache of element references on and off. Default is on.
	 */
	public static boolean CACHE_ELEMENT_REFERENCES = true;
	
	/**
	 * Turn cache of expression types on and off. Default is on.
	 */
	public static boolean CACHE_EXPRESSION_TYPES = true;
}
