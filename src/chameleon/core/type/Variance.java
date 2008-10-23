package chameleon.core.type;

/**
 * @author Marko van Dooren
 * 
 * A class of object representing the variance properties of a generic parameter.
 */
public abstract class Variance {

	/**
	 * Check whether or not a type with the first type as a generic parameter is a subtype
	 * of a type with the second type as generic parameter if the variance of the generic parameter is
	 * set to this object.
	 * 
	 * @param first
	 *        The first type
	 * @param second
	 *        The second type
	 */
 /*@
   @ public behavior
   @
   @ pre first != null;
   @ pre second != null; 
   @*/
	public abstract boolean subTypeOf(Type first, Type second); 
}
