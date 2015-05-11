package org.aikodi.chameleon.core.declaration;

import java.util.List;

import org.aikodi.chameleon.util.Lists;

/**
 * A declarator is an element that introduces declarations.
 * 
 * @author Marko van Dooren
 */
public interface Declarator {

	public default List<Declaration> declarations() {
		return Lists.create();
	}
}
