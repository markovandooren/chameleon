package org.aikodi.chameleon.core.variable;

import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */
public interface VariableContainer extends Element, DeclarationContainer {
	
	/**
	 * Return the element in which the variables are visible. 
	 * 
	 * For example, for a formal parameter of a method, the scope element
	 * is the method, while the parent of the formal parameter is the method header.
	 * For a formal parameter in a catch clause, however, the catch clause is both the parent
	 * and the element that defines the scope of the formal parameter.   
	 * @return
	 */
	public Element variableScopeElement();
	
}
