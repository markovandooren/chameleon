package chameleon.core.tag;

import chameleon.core.element.Element;

public class TagImpl implements Metadata {

	private Element _element;
	
  public final Element getElement() {
  	return _element;
  }
  
  public void setElement(Element element, String name) {
  	if(element != _element) {
  		// Set new pointer, backup old for removal.
  		Element old = _element;
  		_element = element;
  		// Remove from current element.
  		if((_element != null) && (_element.metadata(name) == this)){
  			_element.removeMetadata(name);
  		}
  		// Add to new element.
  		if((_element != null) && (_element.metadata(name) != this)) {
  		  _element.setMetadata(this, name);
  		}
  	}
  }
}
