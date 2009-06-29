package chameleon.core.context;

import chameleon.core.MetamodelException;
import chameleon.core.reference.CrossReference;

/**
 * This is a class of exceptions that report the occurrence of an error during lookup.
 * This class k
 * 
 * 
 * @author marko
 *
 */
public class LookupException extends MetamodelException {

	public LookupException(DeclarationSelector selector) {
		this(null,null,selector);
	}

	public LookupException(String message, Exception exc, DeclarationSelector selector) {
		super(message, exc);
		setSelector(selector);
	}

	public LookupException(String message) {
		this(message,null,null);
	}

	public LookupException(String message, CrossReference ref) {
		this(message,null,null);
		setCrossReference(ref);
	}

	public LookupException(String message, DeclarationSelector selector) {
		this(message,null,selector);
	}

	public LookupException(Exception exc, DeclarationSelector selector) {
		this(null,exc,selector);
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
