package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

/**
 * A predicate selector creates a user interface element
 * and generates a predicate based on the state of
 * that user interface element. A factory is used
 * to ensure that the selector is independent of
 * the UI framework being used.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of objects that can be selected by the generated predicates.
 */
public interface PredicateSelector<T> extends Selector {

	/**
	 * Create a predicate based on the current state of the user interface element.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public UniversalPredicate<? super T, Nothing> predicate();
	
}
