package org.aikodi.chameleon.aspect.core.model.language;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.property.ChameleonProperty;

/**
 * An interface for aspect-oriented programming languages. This
 * interface defines properties that are typical for aspect-oriented programming.
 * 
 * @author Marko van Dooren
 */
public interface AspectOrientedLanguage extends Language {
	
	/**
	 * Return the property the represents "before" advice.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public ChameleonProperty BEFORE();

	/**
	 * Return the property the represents "after" advice.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public ChameleonProperty AFTER();

	/**
	 * Return the property the represents "around" advice.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public ChameleonProperty AROUND();
	
}
