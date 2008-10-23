package chameleon.core.context;


import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.DeclarationSelector;

/**
 * @author Marko van Dooren
 */

public class LexicalContext extends Context {

	//public abstract Context getParentContext() throws MetamodelException;

  public LexicalContext(DeclarationContainer element) {
    super(element);
  }
	
	/**
	 * Return the parent context of this context.
	 * @throws MetamodelException 
	 */
	public LexicalContext parentContext() throws MetamodelException {
	  return element().getParent().lexicalContext(element());
	}

	public <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws MetamodelException {
	  Set<T> tmp = element().declarations(selector);
	  int size = tmp.size();
	  if(size == 1) {
	    return tmp.iterator().next();
	  } else if (size == 0) {
	    return parentContext().lookUp(selector);
	  } else {
	    throw new MetamodelException("Multiple matches found in "+element().toString() + " using selector "+selector.toString());
	  }
	}

}
