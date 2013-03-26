package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;

import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;

/**
 * @author Marko van Dooren
 */

public abstract class ExceptionDeclaration extends ElementImpl {

  public ExceptionDeclaration() {
	}


  public abstract boolean compatibleWith(ExceptionClause clause) throws LookupException;

  public abstract ExceptionDeclaration clone();

  public abstract Set getExceptionTypes(MethodInvocation invocation) throws LookupException;

	/**
	 *
	 * @uml.property name="worstCaseExceptionTypes"
	 */
	public abstract Set getWorstCaseExceptionTypes()
		throws LookupException;


  public abstract boolean hasValidAccessibility() throws LookupException;

}
