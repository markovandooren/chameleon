package chameleon.core.statement;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;

public interface Statement<E extends Statement> extends Element<E>, ExceptionSource<E> {

	public boolean before(Statement other);
	
	public E clone();
	
	public LookupStrategy linearLookupStrategy() throws LookupException;
	
}
