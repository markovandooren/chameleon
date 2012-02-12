package chameleon.eclipse.presentation.treeview;

import chameleon.core.element.Element;
import chameleon.exception.ModelException;

public interface IconProvider {

	/**
	 * Return the name of the icon of the given element. If the icon is not
	 * appropriate for the given element, null is returned. 
	 * @param element
	 * @return
	 * @throws ModelException 
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @*/
	public String iconName(Element<?> element) throws ModelException;
}
