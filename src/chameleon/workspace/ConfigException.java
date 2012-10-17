package chameleon.workspace;

public class ConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8179948468725185835L;

	public ConfigException() {
	}

	public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message,cause,enableSuppression, writableStackTrace);
	}

	public ConfigException(String message, Throwable cause) {
		super(message,cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}
	
}