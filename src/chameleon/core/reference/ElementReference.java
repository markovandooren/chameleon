package chameleon.core.reference;

import org.apache.log4j.Logger;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<E extends ElementReference, P extends Element, R extends Declaration> extends NamespaceElementImpl<E,P>  implements CrossReference<E,P>  {

	private static Logger logger = Logger.getLogger("lookup.elementreference");
	
	public Logger lookupLogger() {
		return logger;
	}
	
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
  	if(Config.CACHE_ELEMENT_REFERENCES == true) {
  	  return _cache;
  	} else {
  		return null;
  	}
  }
  
  protected void setCache(R value) {
  	if(Config.CACHE_ELEMENT_REFERENCES == true) {
  		_cache = value;
  	}
  }
  
  public abstract R getElement() throws LookupException;
}
