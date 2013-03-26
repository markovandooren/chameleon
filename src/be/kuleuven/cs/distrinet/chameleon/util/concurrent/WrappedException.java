package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

public class WrappedException extends RuntimeException {

	public WrappedException() {
		super();
	}

	public WrappedException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrappedException(String message) {
		super(message);
	}

	public WrappedException(Throwable cause) {
		super(cause);
	}
	
}
