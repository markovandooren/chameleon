package be.kuleuven.cs.distrinet.chameleon.oo.statement;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public interface Statement extends Element, ExceptionSource {

	public boolean before(Statement other);
	
//	public Statement clone();
	
	public LookupContext linearLookupStrategy() throws LookupException;
	
}
