package org.aikodi.chameleon.oo.statement;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;

public interface Statement extends Element, ExceptionSource {

	public boolean before(Statement other);
	
//	public Statement clone();
	
	public LookupContext linearLookupStrategy() throws LookupException;
	
}
