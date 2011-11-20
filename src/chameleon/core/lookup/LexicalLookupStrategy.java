package chameleon.core.lookup;


import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.exception.ChameleonProgrammerException;

/**
 * A lexical lookup strategy is used to lookup elements that are not declared relative to another element 
 * using a '.' character. It is the first (and sometimes only) element in such a chain.
 * 
 * The lexical lookup strategy uses the given local lookup strategy to perform
 * a local search. If it does not find anything, it then usually travels upward in the 
 * lexical structure to continue the search. Note that the search does not always continue
 * at the lexical parent, though that is the default behavior. Sometimes, for example, the
 * search must continue at a lexical sibling.
 * 
 * The selector is used to select the next lookup strategy if no element is found.
 * 
 * @author Marko van Dooren
 */

public class LexicalLookupStrategy extends LookupStrategy {

	//public abstract Context getParentContext() throws MetamodelException;

	/**
	 * Initialize a new lexical lookup strategy with the given local strategy and the element
	 * where the lexical search is done. The selector is set to a ParentLookupStrategySelector.
	 */
 /*@
   @ public behavior
   @
   @ post localStrategy() == local;
   @ post nextStrategy() instanceof ParentLookupStrategySelector;
   @ post ((ParentLookupStrategySelector)nextStrategy).element() == element;
  */
	public LexicalLookupStrategy(LookupStrategy local, Element element) {
  	this(local,new ParentLookupStrategySelector(element));
  }
	
	public LexicalLookupStrategy(LookupStrategy local, LookupStrategySelector selector) {
  	if(local == null) {
  		throw new ChameleonProgrammerException("Local context given to lexical context is null");
  	}
  	setLocalContext(local);
  	setSelector(selector);
	}
	
	public void setSelector(LookupStrategySelector selector) {
		_selector = selector;
	}
  
  public LookupStrategySelector selector() {
  	return _selector;
  }
  
  private LookupStrategySelector _selector;

  private void setLocalContext(LookupStrategy local) {
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
	public LookupStrategy nextStrategy() throws LookupException {
		return selector().strategy();
	}

	public <D extends Declaration> void lookUp(DeclarationCollector<D> selector) throws LookupException {
		localContext().lookUp(selector);
		if(selector.willProceed()) {
		  selector.proceed(nextStrategy());
		}
	}
}
