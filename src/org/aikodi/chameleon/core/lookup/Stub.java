package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;

/**
 * This is an interface for stubs that are used as an intermediate parent between elements and their actual "parents".
 * A stub can for example reroute the lookup when an element is inherited and cloned from somewhere else and 
 * the lexical "parent" must be changed. Lookups must be done in the context of the parent of the original element, but
 * lexically, the clone should have a different "parent". The new "parent" will be the parent of the stub while the clone
 * will be the child of the stub.
 *     
 * @author Marko van Dooren
 *
 * @param <E>
 */
public interface Stub extends Element {

	public Element child();
	
	public Element generator();
}
