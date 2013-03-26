package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.language;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.language.AspectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;

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
