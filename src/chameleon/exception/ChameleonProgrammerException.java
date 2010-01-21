package chameleon.exception;

/**
 * A ChameleonProgrammerException is used to signal an exception because of a bug in Chameleon or
 * a concrete metamodel. If the bug can result from user input, like e.g. an incomplete model, 
 * a {@link ModelException} MetamodelException should be used instead.
 * 
 * @author Marko van Dooren
 */
public class ChameleonProgrammerException extends ChameleonRuntimeException {

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public ChameleonProgrammerException() {
	}
	
 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public ChameleonProgrammerException(String msg) {
		super(msg);
	}
	
 /*@
   @ public behavior
   @
   @ post getMessage() == null;
   @ post getCause().equals(e);
   @*/
	public ChameleonProgrammerException(Exception e) {
		super(e);
	}

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause().equals(e);
   @*/
	public ChameleonProgrammerException(String msg, Exception e) {
		super(msg,e);
	}
	
}
