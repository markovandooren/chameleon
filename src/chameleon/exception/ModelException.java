package chameleon.exception;


/**
 * @author marko
 */
public class ModelException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2841949559634528013L;

	public ModelException() {
  }
  
  public ModelException(String message) {
    super(message);
  }
  
  public ModelException(String message, Exception exc) {
  	super(message, exc);
  }
  
}
