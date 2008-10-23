package chameleon.core.context;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;

/**
 * A class of factories that create context objects.
 * 
 * @author Marko van Dooren
 */
public class ContextFactory {

	public LexicalContext createLexicalContext(DeclarationContainer element) {
		return new LexicalContext(element);
	}
	
  public TargetContext createTargetContext(DeclarationContainer element) {
  	return new TargetContext(element);
  }
	
}
