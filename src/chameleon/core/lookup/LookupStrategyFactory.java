package chameleon.core.lookup;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;

/**
 * A class of factories that create context objects.
 * 
 * @author Marko van Dooren
 */
public class LookupStrategyFactory {

	public LookupStrategy createLexicalContext(Element element, LookupStrategy local) {
		return new LexicalLookupStrategy(local, element);
	}
	
  public  LookupStrategy createTargetContext(DeclarationContainer element) {
  	return new LocalLookupStrategy(element);
  }

	public LookupStrategy wrapLocalStrategy(LookupStrategy targetContext, Element element) {
		return targetContext;
	}
  
}
