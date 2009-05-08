package chameleon.core.reference;

import chameleon.core.Config;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.namespacepart.NamespacePartElementImpl;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<E extends ElementReference,R extends Element> extends NamespacePartElementImpl<E,Element>  implements CrossReference<E,Element>  {

  //@FIXME: merge with CrossReference 
  
	public ElementReference() {
	}
	
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
	public ElementReference(String name) {
		setName(name);
	}
	

 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public String getName() {
    return _name;
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
  public void setName(String name) {
  	if(name == null) {
  		throw new ChameleonProgrammerException("Name of an element reference is being set to null");
  	}
  	_name = name;
  }
 
  private String _name;

  private R _cache;
  
  protected R getCache() {
  	return _cache;
  }
  
  protected void setCache(R value) {
  	if(Config.CACHE_ELEMENT_REFERENCES == true) {
  		_cache = value;
  	}
  }
  
}
