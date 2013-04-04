/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public class ParentLookupContextSelector implements LookupContextSelector {
	
	public ParentLookupContextSelector(Element element) {
		setElement(element);
	}
	
  public void setElement(Element element) {
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
	public LookupContext strategy() throws LookupException {
		Element element = element();
		Element parent = element.parent();
		if(parent != null) {
	    return parent.lookupContext(element);
		} else {
			return null;
		}
	}

}
