package chameleon.core.context;


import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationSelector;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */

public class LexicalContext extends Context {

	//public abstract Context getParentContext() throws MetamodelException;

  public LexicalContext(Context local, Element element) {
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
  
  public void setLocalContext(Context local) {
  	_localContext = local;
  }
  
  public Context localContext() {
  	return _localContext;
  }
  
  private Context _localContext;
  
  
  
	/**
	 * Return the parent context of this context.
	 * @throws MetamodelException 
	 */
	public Context parentContext() throws MetamodelException {
	  return element().getParent().lexicalContext(element());
	}

	public <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws MetamodelException {
	  T tmp = localContext().lookUp(selector);
	  if(tmp != null) {
	    return tmp;
	  } else {
	    return parentContext().lookUp(selector);
	  }
	}

}
