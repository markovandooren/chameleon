package be.kuleuven.cs.distrinet.chameleon.core.rule;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.language.WrongLanguageException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
/**
 * A class of rules that apply to a language. The semantics of the rules is not determined in this class.
 * 
 * @author Marko van Dooren
 *
 * @param <R> A self type. R must always be a supertype of the class itself. This allows us to define the association at this level.
 */
public abstract class Rule<R extends Rule, E extends Element> {
	
	public Rule(Class<E> elementType) {
		_elementType = elementType;
	}
	
	public Class<E> elementType() {
		return _elementType;
	}
	
	private Class<E> _elementType;

	private SingleAssociation<R,Language> _language = new SingleAssociation<R, Language>((R) this);
	
	public SingleAssociation<R,Language> languageLink() {
		return _language;
	}
	
	/**
	 * Return the language to which this rule applies.
	 */
	public Language language() {
		return _language.getOtherEnd();
	}

  /**
   * Return the language of this element if it is of the wrong kind. Return null if this element is not
   * connected to a complete model. Throws a WrongLanguageException is the
   * language is not of the correct type.
   */
 /*@
   @ public behavior
   @
   @ pre kind != null;
   @
   @ post \result == language();
   @
   @ signals(WrongLanguageException) language() != null && ! kind.isInstance(language());
   @*/
  public <T extends Language> T language(Class<T> kind) {
  	if(kind == null) {
  		throw new ChameleonProgrammerException("The given language class is null.");
  	}
  	Language language = language();
  	if(kind.isInstance(language) || language == null) {
  		return (T) language;
  	} else {
  		throw new WrongLanguageException("The language of this model is of the wrong kind. Expected: "+kind.getName()+" but got: " +language.getClass().getName());
  	}
  }


}
