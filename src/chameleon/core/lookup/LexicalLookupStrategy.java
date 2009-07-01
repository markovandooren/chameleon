package chameleon.core.lookup;


import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */

public class LexicalLookupStrategy extends LookupStrategy {

	//public abstract Context getParentContext() throws MetamodelException;

  public LexicalLookupStrategy(LookupStrategy local, Element element) {
  	setLocalContext(local);
  	setElement(element);
  }
  
  public void setElement(Element element) {
  	_element=element;
  }
  
  public Element element() {
  	return _element;
  }
  
  private Element _element;
  
  public void setLocalContext(LookupStrategy local) {
  	_localContext = local;
  }
  
  public LookupStrategy localContext() {
  	return _localContext;
  }
  
  private LookupStrategy _localContext;
  
  
  
	/**
	 * Return the parent context of this context.
	 * @throws LookupException 
	 */
	public LookupStrategy parentContext() throws LookupException {
		Element parent = element().parent();
		if(parent != null) {
	    return parent.lexicalContext(element());
		} else {
			throw new LookupException("Lookup wants to go to the parent element of a "+element().getClass() +" but it is null.");
		}
		
	}

	public <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws LookupException {
			T tmp = localContext().lookUp(selector);
			if(tmp != null) {
			  return tmp;
			} else {
			  return parentContext().lookUp(selector);
			}
	}

}
