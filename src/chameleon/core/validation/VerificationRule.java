package chameleon.core.validation;

import chameleon.core.element.Element;
import chameleon.core.rule.Rule;

/**
 * A class representing validity rules for model elements.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public abstract class VerificationRule<E extends Element> extends Rule<VerificationRule, E> {

	/**
	 * Create a new validity rule for elements of the given type.
	 * @param elementType The type of the elements to which this rule applies.
	 */
 /*@
   @ public behavior
   @
   @ pre elementType != null;
   @
   @ post elementType() == elementType;
   @*/
	public VerificationRule(Class<E> elementType) {
		super(elementType);
	}

	/**
	 * Verify the given element.
	 * 
	 * @param element The element to be verified.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @*/
	public abstract VerificationResult verify(E element);

}
