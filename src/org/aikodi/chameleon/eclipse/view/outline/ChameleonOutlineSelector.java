package org.aikodi.chameleon.eclipse.view.outline;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.exception.ModelException;

import java.util.List;

public class ChameleonOutlineSelector {

	public List<Element> outlineChildren(Element element) throws ModelException {
		return (List<Element>) element.lexical().children();
	}
	
	/**
	 * Check whether the given element is allowed to be in the outline tree.
	 * 
	 * By default only declarations are allowed.
	 */
	public boolean isAllowed(Element element) throws ModelException {
		return element instanceof Declaration;
	}
}
