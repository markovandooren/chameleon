package org.aikodi.chameleon.exception;

/**
 * A class of runtime exceptions that can occur during the execution of Chameleon code.
 * You should always use checked exceptions if the problem caused is user error (e.g. when the
 * model is incomplete).
 * 
 * @author Marko van Dooren
 */
public abstract class ChameleonRuntimeException extends RuntimeException {
/*@
  @ public behavior
  @
  @ post getMessage().equals(msg);
  @ post getCause() == null;
  @*/
	public ChameleonRuntimeException() {
	}
	
/*@
  @ public behavior
  @
  @ post getMessage().equals(msg);
  @ post getCause() == null;
  @*/
	public ChameleonRuntimeException(String msg) {
		super(msg);
	}
	
/*@
  @ public behavior
  @
  @ post getMessage() == null;
  @ post getCause().equals(e);
  @*/
	public ChameleonRuntimeException(Exception e) {
		super(e);
	}

/*@
  @ public behavior
  @
  @ post getMessage().equals(msg);
  @ post getCause().equals(e);
  @*/
	public ChameleonRuntimeException(String msg, Exception e) {
		super(msg,e);
	}

}
