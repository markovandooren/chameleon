package be.kuleuven.cs.distrinet.chameleon.core.language;

import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * This class of exceptions signals a mismatch between the language of an element
 * and the type of language that it expects.
 * 
 * Statically enforcing this would make the burden of generics even higher. In addition, that approach
 * would only help if generic parameters are always filled in, which is impossible for recursively constrained
 * parameters in some cases. We therefore opted to throw a WrongLanguageException, which is a subclass of
 * ChameleonProgrammerException. If the language is wrong, that is an error in the language module.
 * 
 * @author Marko van Dooren
 */
public class WrongLanguageException extends ChameleonProgrammerException {

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public WrongLanguageException() {
	}

 /*@
   @ public behavior
   @
   @ post getMessage() == null;
   @ post getCause().equals(e);
   @*/
	public WrongLanguageException(Exception e) {
		super(e);
	}

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause().equals(e);
   @*/
	public WrongLanguageException(String msg, Exception e) {
		super(msg,e);
	}

 /*@
   @ public behavior
   @
   @ post getMessage().equals(msg);
   @ post getCause() == null;
   @*/
	public WrongLanguageException(String msg) {
		super(msg);
	}

}
