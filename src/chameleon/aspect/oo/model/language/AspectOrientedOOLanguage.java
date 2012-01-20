package chameleon.aspect.oo.model.language;

import chameleon.aspect.core.model.language.AspectOrientedLanguage;
import chameleon.core.property.ChameleonProperty;

public interface AspectOrientedOOLanguage extends AspectOrientedLanguage {
	
	/**
	 * Return the property the represents "after throwing" advice.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public ChameleonProperty THROWING();

	/**
	 * Return the property the represents "after returning" advice.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public ChameleonProperty RETURNING();

}
