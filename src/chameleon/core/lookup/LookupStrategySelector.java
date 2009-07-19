/**
 * 
 */
package chameleon.core.lookup;

public interface LookupStrategySelector {
	public LookupStrategy strategy() throws LookupException;
}