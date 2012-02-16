package chameleon.oo.statement;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;

public interface Statement extends Element, ExceptionSource {

	public boolean before(Statement other);
	
	public Statement clone();
	
	public LookupStrategy linearLookupStrategy() throws LookupException;
	
}
