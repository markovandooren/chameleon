package chameleon.core.lookup;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;

/**
 * A class of factories that create lookup strategies.
 * 
 * @author Marko van Dooren
 */
public class LookupStrategyFactory {

	/**
	 * A lexical lookup is used for an element that is not declared relative to another element 
	 * using a '.' character. It is the first (and sometimes only) element in such a chain.
	 * 
	 * The lexical lookup strategy uses the given local lookup strategy to perform
	 * a local search. If it does not find anything, it then usually travels upward in the 
	 * lexical structure to continue the search. Note that the search does not always continue
	 * at the lexical parent, though that is the default behavior. Sometimes, for example, the
	 * search must continue at a lexical sibling.
	 * 
	 * By default, this method returns a LexicalLookupStrategy object.
	 * @param local
	 *        The local lookup strategy that is used to perform the local search.
	 * @param element
	 *        The element in which the lexical search will be performed. This element is
	 *        used to decide which kind of lookup strategy to return. It is not always the
	 *        element in which the local search will be performed.
	 * 
	 * @return
	 */
	public LookupStrategy createLexicalLookupStrategy(LookupStrategy local, Element element) {
		return new LexicalLookupStrategy(local, element);
	}
	
	/**
	 * A lexical lookup is used for an element that is not declared relative to another element 
	 * using a '.' character. It is the first (and sometimes only) element in such a chain.
	 * 
	 * The lexical lookup strategy uses the given local lookup strategy to perform
	 * a local search. If it does not find anything, it then usually travels upward in the 
	 * lexical structure to continue the search. 
	 * 
	 * The given selector determines which strategy is used if the element cannot be found by
	 * the local context.
	 * 
	 * By default, this method returns a LexicalLookupStrategy object.
	 * @param local
	 *        The local lookup strategy that is used to perform the local search.
	 * @param element
	 *        The selector that determines which strategy is used if the element cannot be found by
	 *        the local context.
	 * 
	 * @return
	 */
	public LookupStrategy createLexicalLookupStrategy(LookupStrategy local, Element element, LookupStrategySelector selector) {
		return new LexicalLookupStrategy(local, selector);
	}
	
  public  LocalLookupStrategy createTargetLookupStrategy(DeclarationContainer element) {
  	return new LocalLookupStrategy(element);
  }

  public  LookupStrategy createLocalLookupStrategy(DeclarationContainer element) {
  	return new LocalLookupStrategy(element);
  }
  
}
