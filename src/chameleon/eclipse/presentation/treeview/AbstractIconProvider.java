package chameleon.eclipse.presentation.treeview;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;

public abstract class AbstractIconProvider implements IconProvider {
	/**
	 * Initialize a new default icon provider with the given element type. The
	 * list of decorators is empty.
	 * 
	 * @param baseIconName
	 * @param elementType
	 */
 /*@
   @ public behavior
   @
   @ pre elementType != null;
   @
   @ post elementType() == elementType;
   @ post decorators().isEmpty();
   @*/
	public AbstractIconProvider(Class<? extends Element> elementType) {
		if(elementType == null) {
			throw new ChameleonProgrammerException();
		}
		_elementType = elementType;
	}
	
	/**
	 * Initialize a new default icon provide with the given element type and decorator.
	 * 
	 * @param baseIconName
	 * @param elementType
	 */
 /*@
   @ public behavior
   @
   @ pre elementType != null;
   @ pre decorator != null;
   @
   @ post elementType() == elementType;
   @ post decorators().size() == 1;
   @ post decorators().contains(decorator);
   @*/
	public AbstractIconProvider(Class<? extends Element> elementType, NameBasedIconDecorator... decorators) {
		this(elementType);
		for(NameBasedIconDecorator decorator: decorators) {
		  add(decorator);
		}
	}

	/**
	 * Return the icon name for the given element. If no icon can be provided for the element,
	 * null is returned. The name is decorated.
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post \result == null | \result.equals(decoratedName(element, baseIconName()));
   @*/
	public String iconName(Element<?> element) throws ModelException {
		String result = null;
		if(elementType().isInstance(element)) {
			result = baseIconName(element);
			if(result != null) {
				result = decoratedName(element, result);
			}
		}
		return result;
	}
	
	/**
	 * Return the decorated name for the given element with the given base icon name.
	 * @param element
	 * @param baseIconName
	 * @return
	 * @throws ModelException
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @ pre baseIconName != null;
   @
   @ post \result.equals(decoratedName(element, baseIconName, decorators()));
   @*/
	public String decoratedName(Element<?> element, String baseIconName) throws ModelException {
		return decoratedName(element, baseIconName, decorators());
	}
	
	/**
	 * Return the decorated name for the given element with the given base icon name and decorators.
	 * @param element
	 * @param baseIconName
	 * @return
	 * @throws ModelException
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @ pre baseIconName != null;
   @ pre decorators != null;
   @
   @ post decorators.isEmpty() ==> \result == baseIconName;
   @ post !decorators.isEmpty() ==> \result.equals(decorators.get(decorators.size()-1).decorate(element, decoratedName(element,baseIconName,decorators.subList(0, decorators.size()-1))));
   @*/
	public String decoratedName(Element<?> element, String baseIconName, List<NameBasedIconDecorator> decorators) throws ModelException {
		String result = baseIconName;
		for(NameBasedIconDecorator decorator: decorators) {
			result = decorator.decorate(element, result);
		}
		return result;
	}
	
	/**
	 * Return the base icon name for the given element. If the element is not of the element type
	 * of this icon provider, null is returned.
	 * @param element
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post elementType().isInstance(element) ==>  \result.equals(baseIconName());
   @ post ! elementType().isInstance(element) ==> \result == null;  
   @*/
	public abstract String baseIconName(Element<?> element);

	/**
	 * Add a new decorator to his icon provider.
	 * @param decorator
	 */
 /*@
   @ public behavior
   @
   @ pre decorator != null;
   @ pre ! decorators().contains(decorator);
   @
   @ post decorators().size() == \old(decorators().size()) + 1;
   @ post decorators.get(decorators().size()-1) == decorator;
   @ post (\forall int i; i>=0 && i < \old(decorators().size()); \old(decorators().get(i)) == decorators.get(i)); 
   @*/
	public void add(NameBasedIconDecorator decorator) {
		if(decorator != null && ! _decorators.contains(decorator)) {
		  _decorators.add(decorator);
		} else {
			throw new ChameleonProgrammerException();
		}
	}
	
	/**
	 * Return the decorators of this icon provider.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<NameBasedIconDecorator> decorators() {
		return new ArrayList<NameBasedIconDecorator>(_decorators);
	}
	
	private List<NameBasedIconDecorator> _decorators = new ArrayList<NameBasedIconDecorator>();
	
	/**
	 * Return the element type for which this icon provider can provide icons.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Class<? extends Element> elementType() {
		return _elementType;
	}
	
	private Class<? extends Element> _elementType;

}
