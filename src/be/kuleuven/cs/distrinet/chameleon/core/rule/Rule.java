package be.kuleuven.cs.distrinet.chameleon.core.rule;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.language.WrongLanguageException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
/**
 * A class of rules that apply to a language. The semantics of the rules is not 
 * determined in this class.
 * 
 * @author Marko van Dooren
 *
 * @param <R> A self type. R must always be a supertype of the class itself. 
 * This allows us to define the association at this level.
 */
public abstract class Rule<R extends Rule<R,E>, E extends Element> {
	
   /**
    * Create a new rule for the given type of elements.
    * 
    * @param elementType The type of elements to which this rule applies.
    */
  /*@
    @ public behavior
    @
    @ post elementType() == elementType;
    @*/
	public Rule(Class<E> elementType) {
		_elementType = elementType;
	}
	
	/**
	 * @return The type of elements to which this rule applies.
	 */
	public Class<E> elementType() {
		return _elementType;
	}
	
	private Class<E> _elementType;

	private SingleAssociation<R,Language> _language = new SingleAssociation<R, Language>((R) this);
	
	/**
	 * @return The association end that links this rule to the language.
	 */
	public SingleAssociation<R,Language> languageLink() {
		return _language;
	}
	
	/**
	 * @return the language to which this rule applies.
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
   @ pre type != null;
   @
   @ post \result == language();
   @
   @ signals(WrongLanguageException) language() != null && ! type.isInstance(language());
   @*/
  public <T extends Language> T language(Class<T> type) {
  	if(type == null) {
  		throw new ChameleonProgrammerException("The given language class is null.");
  	}
  	Language language = language();
  	if(type.isInstance(language) || language == null) {
  		return (T) language;
  	} else {
  		throw new WrongLanguageException("The language of this model is of the wrong kind. Expected: "+type.getName()+" but got: " +language.getClass().getName());
  	}
  }


}
