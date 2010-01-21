package chameleon.core.visitor;

import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;

public class NoVisitorForTypeException extends ChameleonProgrammerException {

	private Element _e;
	private static String errorMessage = "Visitor is not well-defined for this type of element.";

	public String getTypeName(){
		return getType().getName();
	}
	
	public Class getType(){
		return getElement().getClass();
	}
	
	public Element getElement(){
		return _e;
	}
	  
	  public NoVisitorForTypeException(Element e) {
	    super(errorMessage);
	    _e = e;
	  }
	  
	  public NoVisitorForTypeException(Element e, Exception cause) {
	  	super(errorMessage, cause);
	  	_e = e;
	  }
	
}
