package chameleon.core.context;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;

/**
 * A class of factories that create context objects.
 * 
 * @author Marko van Dooren
 */
public class ContextFactory {

	public LexicalContext createLexicalContext(Element element, Context local) {
		return new LexicalContext(local, element);
	}
	
  public <E extends DeclarationContainer> TargetContext<E> createTargetContext(E element) {
  	return new TargetContext(element);
  }
	
}
