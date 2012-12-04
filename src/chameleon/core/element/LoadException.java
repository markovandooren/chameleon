package chameleon.core.element;

public class LoadException extends RuntimeException {

	/**
	 * <p>A LoadException signals a fault when loading a part of the
	 * lexical structure of the model. This typically occurs when
	 * a part of the model is loaded lazily and an I/O error occurs.</p>
	 * <p>If lazy loading is not used, this exception should not occur as
	 * traversing the lexical structure cannot go wrong if all objects are loaded.</p>
	 */
	private static final long serialVersionUID = -7632391753572389316L;

	/**
	 * Initialize a new load exception. 
	 */
 /*@
   @ public behavior
   @
   @ post getMessage() == null;
   @ post getCause() == null;
   @*/
	public LoadException() {
	}

	/**
	 * Initialize a new load exception with the given message and cause. 
	 */
 /*@
   @ public behavior
   @
   @ post getMessage() == message;
   @ post getCause() == cause;
   @*/
	public LoadException(String message, Throwable cause) {
		super(message,cause);
	}

	/**
	 * Initialize a new load exception with the given message. 
	 */
 /*@
   @ public behavior
   @
   @ post getMessage() == message;
   @ post getCause() == null;
   @*/
	public LoadException(String message) {
		super(message);
	}

	/**
	 * Initialize a new load exception with the given cause. 
	 */
 /*@
   @ public behavior
   @
   @ post getMessage() == null;
   @ post getCause() == cause;
   @*/
	public LoadException(Throwable cause) {
		super(cause);
	}

}
