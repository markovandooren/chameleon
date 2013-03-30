/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

public interface LookupContextSelector {
	public LookupContext strategy() throws LookupException;
}
