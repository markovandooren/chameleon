package chameleon.core.statement;

import chameleon.core.method.exception.ExceptionDeclaration;
import chameleon.core.type.Type;

/**
 * A class of pairs (checked exception type, exception declaration). These are the elements of a
 * checked exception list(CEL).
 * 
 * @author Marko van Dooren
 */
public class ExceptionTuple {
  
	 /*@
	   @ public behavior
	   @
	   @ post getException() == exception;
	   @ post getDeclaration() == declaration;
	   @*/
	  public ExceptionTuple(Type exception, ExceptionDeclaration declaration, ExceptionSource cause) {
	    _exception = exception;
	    _declaration = declaration;
	    _cause = cause;
	  }

	  private ExceptionSource _cause;
	  
	  public ExceptionSource getCause() {
	    return _cause;
	  }

	  /**
	   * Return the type of exception of this pair.
	   */
	 /*@
	   @ public behavior
	   @
	   @ post \result != null
	   @*/
	  public Type getException() {
	    return _exception;
	  }
	  
	  private Type _exception;
	  
	  private ExceptionDeclaration _declaration;
	  
	 /*@
	   @ public behavior
	   @
	   @ post \result != null
	   @*/
	  public ExceptionDeclaration getDeclaration() {
	    return _declaration;
	  }
}
