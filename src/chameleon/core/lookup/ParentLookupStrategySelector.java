/**
 * 
 */
package chameleon.core.lookup;

import chameleon.core.element.Element;

public class ParentLookupStrategySelector implements LookupStrategySelector {
	
	public ParentLookupStrategySelector(Element element) {
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
	public LookupStrategy strategy() throws LookupException {
		Element parent = element().parent();
		if(parent != null) {
	    return parent.lexicalContext(element());
		} else {
			throw new LookupException("Lookup wants to go to the parent element of a "+element().getClass() +" but it is null.");
		}
		
	}

}