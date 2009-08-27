package chameleon.input;

import chameleon.core.compilationunit.CompilationUnit;

public class ParseException extends Exception {

	  public ParseException(CompilationUnit compilationUnit) {
	  	this(null,null,compilationUnit);
	  }
	  
	  public ParseException(String msg,CompilationUnit compilationUnit) {
	  	this(msg,null,compilationUnit);
	  }
	  public ParseException(Exception exc,CompilationUnit compilationUnit) {
	  	this(null,exc,compilationUnit);
	  }
	  public ParseException(String msg, Exception exc,CompilationUnit compilationUnit) {
	  	super(msg, exc);
	  	_compilationUnit = compilationUnit;
	  }
	  
	  private CompilationUnit _compilationUnit;
	  
	  public CompilationUnit compilationUnit() {
	  	return _compilationUnit;
	  }
}
