package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;

/**
 * A class of factories that create lookup strategies.
 * 
 * @author Marko van Dooren
 */
public class LookupContextFactory {

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
	public LookupContext createLexicalLookupStrategy(LookupContext local, Element element) {
//		report(element);
		return new LexicalLookupContext(local, element);
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
	public LookupContext createLexicalLookupStrategy(LookupContext local, Element element, LookupContextSelector selector) {
//		report(element);
		return new LexicalLookupContext(local, selector);
	}
	
  public  LocalLookupContext createTargetLookupStrategy(DeclarationContainer element) {
//  	report(element);
  	return new LocalLookupContext(element);
  }

  public  LookupContext createLocalLookupStrategy(DeclarationContainer element) {
//  	report(element);
  	return new LocalLookupContext(element);
  }

//  public static boolean ENABLED=true;
//  
//  protected void report(Element element) {
//  	if(ENABLED) {
//  		if(LEXICAL_DONE.contains(element)) {
//  			Integer current = LEXICAL_ALLOCATORS.get(element.getClass());
//  			Integer newValue = current == null ? 1 : current + 1;
//  			LEXICAL_ALLOCATORS.put(element.getClass(), newValue);
//  		} else {
//  			LEXICAL_DONE.add(element);
//  		}
//  	}
//  }
//  
//  public final static Map<Class,Integer> LEXICAL_ALLOCATORS = new HashMap<>();
//  
//  public final static Set<Element> LEXICAL_DONE = new HashSet<>();
  
}
