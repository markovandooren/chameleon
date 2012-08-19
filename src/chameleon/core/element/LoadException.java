package chameleon.core.element;

public class LoadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7632391753572389316L;

	public LoadException() {
	}

	public LoadException(String message, Throwable cause) {
		super(message,cause);
	}

	public LoadException(String message) {
		super(message);
	}

	public LoadException(Throwable cause) {
		super(cause);
	}

}
