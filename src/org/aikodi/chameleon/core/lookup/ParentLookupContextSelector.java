/**
 * 
 */
package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.element.Element;

public class ParentLookupContextSelector implements LookupContextSelector {
	
	public ParentLookupContextSelector(Element element) {
  	_element=element;
	}
	
  public Element element() {
  	return _element;
  }
  
  private Element _element;

	/**
	 * Return the parent context of this context.
	 * @throws LookupException 
	 */
	@Override
   public LookupContext strategy() throws LookupException {
		Element element = element();
		Element parent = element.lexical().parent();
		if(parent != null) {
	    return parent.lookupContext(element);
		} else {
			return null;
		}
	}

}
