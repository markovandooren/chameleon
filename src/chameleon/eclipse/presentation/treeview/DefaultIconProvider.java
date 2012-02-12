package chameleon.eclipse.presentation.treeview;

import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;

/**
 * A class of icon providers that map elements of a certain type to icon names. A default icon provider
 * has an optional list of decorators that influence the mapping.
 * 
 * @author Marko van Dooren
 */
public class DefaultIconProvider extends AbstractIconProvider {

	//FIXME replace baseIconName with Icon, which has a name? That way the name is not written in two places. 
	
	public DefaultIconProvider(String baseIconName, Class<? extends Element> elementType, NameBasedIconDecorator... decorators) {
		super(elementType, decorators);
		if(baseIconName == null) {
			throw new ChameleonProgrammerException();
		}
		_baseIconName = baseIconName;
	}
	
	public String baseIconName(Element element) {
		String result = null;
		if(elementType().isInstance(element)) {
			result = baseIconName();
		}
		return result;
	}

	/**
	 * Return the base icon name.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String baseIconName() {
		return _baseIconName;
	}
	
  private String _baseIconName;
}
