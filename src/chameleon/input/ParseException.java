package chameleon.input;

import chameleon.core.compilationunit.Document;

public class ParseException extends Exception {

	  public ParseException(Document compilationUnit) {
	  	this(null,null,compilationUnit);
	  }
	  
	  public ParseException(String msg,Document compilationUnit) {
	  	this(msg,null,compilationUnit);
	  }
	  public ParseException(Exception exc,Document compilationUnit) {
	  	this(null,exc,compilationUnit);
	  }
	  public ParseException(String msg, Exception exc,Document compilationUnit) {
	  	super(msg, exc);
	  	_compilationUnit = compilationUnit;
	  }
	  
	  private Document _compilationUnit;
	  
	  public Document compilationUnit() {
	  	return _compilationUnit;
	  }
}
