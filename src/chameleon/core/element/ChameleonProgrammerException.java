package chameleon.core.element;

/**
 * A ChameleonProgrammerException is used to signal an exception because of a bug in Chameleon or
 * a concrete metamodel. If the bug can result from user input, like e.g. an incomplete model, 
 * a {@link MetamodelException} MetamodelException should be used instead.
 * 
 * @author marko
 */
public class ChameleonProgrammerException extends RuntimeException {

	
	public ChameleonProgrammerException() {
	}
	
	public ChameleonProgrammerException(String msg) {
		super(msg);
	}
	
	public ChameleonProgrammerException(Exception e) {
		super(e);
	}
}
