package be.kuleuven.cs.distrinet.chameleon.oo.method;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;

/**
 * @author Marko van Dooren
 */

public abstract class Implementation extends ElementImpl {

  public Implementation() {
    }

  /**
   * Check if the implementation exception clause of the method body 
   * conforms to the exception clause of the parent method.
   * @return
   */
  public abstract boolean compatible() throws LookupException;

  /**
   * Check whether or not all catch blocks in the implementation are useful. If a catch block
   * catches a checked exception that can never be thrown in the try statement, that catch block
   * is useless and should be removed.
   */
  public boolean hasValidCatchClauses() throws LookupException {
    return true;
  }

  /**
   * Return the body of this implementation.
   */
	public abstract Block getBody();

	public abstract boolean complete();


}
