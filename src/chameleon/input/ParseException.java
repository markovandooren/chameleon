package chameleon.input;

public class ParseException extends Exception {

	  public ParseException() {
	  	
	  }
	  public ParseException(String msg) {
	  	super(msg);
	  }
	  public ParseException(Exception exc) {
	  	super(exc);
	  }
	  public ParseException(String msg, Exception exc) {
	  	super(msg, exc);
	  }
	  
}
