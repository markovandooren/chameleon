package chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;

/**
 * 
 * @author Marko van Dooren
 */
public class LocalLookupStrategy<E extends DeclarationContainer> extends LookupStrategy {

	public LocalLookupStrategy(E element) {
		_element = element;
	}
	
	private E _element;

	/**
	 * Return the element referenced by this collector
	 * @return
	 */
	public E element() {
	  return _element; 
	}

  /**
   * Return those declarations of this declaration container that are selected
   * by the given declaration selector. The default implementation delegates the work
   * to element().declarations(selector).
   * 
   * @param <D> The type of the arguments selected by the given signature selector. This type
   *            shoud be inferred automatically.
   * @param selector
   * @return
   * @throws LookupException
   */
  protected <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
  	return element().declarations(selector);
  }

	@Override
	public <D extends Declaration> D lookUp(DeclarationSelector<D> selector) throws LookupException {
	  List<D> tmp = declarations(selector);
	  int size = tmp.size();
	  if(size == 1) {
	    return tmp.iterator().next();
	  } else if (size == 0) {
	    return null;
	  } else {
	  	// Disable declaration cache before we go debugging.
//	  	Config.CACHE_DECLARATIONS = false;
//	  	tmp = declarations(selector);
	    throw new LookupException("Multiple matches found in "+element().toString() + " using selector "+selector.toString(),selector);
	  }

	}

}
