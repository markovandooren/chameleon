package be.kuleuven.cs.distrinet.chameleon.plugin.build;

public class BuildException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8806567820477254746L;

	public BuildException() {
	}

	public BuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

}
