package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.exception.ModelException;

/**
 * This is a class of exceptions that report the occurrence of an error during lookup.
 * This class k
 * 
 * 
 * @author marko
 *
 */
public class LookupException extends ModelException {

	public LookupException(String message, Exception exc, DeclarationSelector selector) {
		super(message, exc);
		setSelector(selector);
	}

	//49
	public LookupException(String message) {
		this(message,null,null);
	}

	//5
	public LookupException(String message, Exception exc) {
		this(message,exc,null);
	}

	//2
	public LookupException(String message, DeclarationSelector selector) {
		this(message,null,selector);
	}

	public void setSelector(DeclarationSelector selector) {
		_selector = selector;
	}
	
	public DeclarationSelector selector() {
		return _selector;
	}
	
	private DeclarationSelector _selector;
	private CrossReference _crossReference;
	
	public void setCrossReference(CrossReference ref) {
		_crossReference = ref;
	}
}
