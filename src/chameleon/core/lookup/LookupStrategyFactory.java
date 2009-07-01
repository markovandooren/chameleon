package chameleon.core.lookup;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;

/**
 * A class of factories that create context objects.
 * 
 * @author Marko van Dooren
 */
public class LookupStrategyFactory {

	public LexicalLookupStrategy createLexicalContext(Element element, LookupStrategy local) {
		return new LexicalLookupStrategy(local, element);
	}
	
  public <E extends DeclarationContainer> LocalLookupStrategy<E> createTargetContext(E element) {
  	return new LocalLookupStrategy(element);
  }
	
}
