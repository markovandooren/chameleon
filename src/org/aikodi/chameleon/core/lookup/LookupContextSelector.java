/**
 * 
 */
package org.aikodi.chameleon.core.lookup;


public interface LookupContextSelector {
	public LookupContext strategy() throws LookupException;
}
