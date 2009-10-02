package chameleon.core.statement;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;

public interface Statement<E extends Statement, P extends Element> extends Element<E, P>, ExceptionSource<E,P> {

	public boolean before(Statement other);
	
	public E clone();
	
	public LookupStrategy linearContext() throws LookupException;
	
}
