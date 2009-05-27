package chameleon.core.variable;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.type.TypeDescendant;

/**
 * @author Marko van Dooren
 */
public interface VariableContainer<E extends VariableContainer, P extends Element> extends TypeDescendant<E,P>, DeclarationContainer<E,P>
{
	
	/**
	 * Return the element in which the variables are visible. 
	 * 
	 * For example, for a formal parameter of a method, the scope element
	 * is the method, while the parent of the formal parameter is the method header.
	 * For a formal parameter in a catch clause, however, the catch clause is both the parent
	 * and the element that defines the scope of the formal parameter.   
	 * @return
	 */
	public NamespaceElement variableScopeElement();
	
}
