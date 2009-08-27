package chameleon.core;


/**
 * @author marko
 */
public class MetamodelException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2841949559634528013L;

	public MetamodelException() {
  }
  
  public MetamodelException(String message) {
    super(message);
  }
  
  public MetamodelException(String message, Exception exc) {
  	super(message, exc);
  }
  
}
