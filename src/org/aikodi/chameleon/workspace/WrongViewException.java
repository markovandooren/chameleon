package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * This class of exceptions signals a mismatch between the view of an element
 * and the type of language that it expects.
 * 
 * Statically enforcing this would make the burden of generics very high. In addition, that approach
 * would only help if generic parameters are always filled in, which is impossible for recursively constrained
 * parameters in some cases. We therefore opted to throw a WrongViewException, which is a subclass of
 * ChameleonProgrammerException.
 * 
 * @author Marko van Dooren
 */
public class WrongViewException extends ChameleonProgrammerException {

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public WrongViewException() {
	}

 /*@
   @ public behavior
   @
   @ post getMessage() == null;
   @ post getCause().equals(e);
   @*/
	public WrongViewException(Exception e) {
		super(e);
	}

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause().equals(e);
   @*/
	public WrongViewException(String msg, Exception e) {
		super(msg,e);
	}

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public WrongViewException(String msg) {
		super(msg);
	}

}
