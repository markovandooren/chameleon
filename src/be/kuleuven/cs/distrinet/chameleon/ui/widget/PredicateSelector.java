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
public abstract class PredicateSelector<T>  {

	/**
	 * Create a control with the given factory.
	 * 
	 * @param factory The factory that creates the user interface element.
	 */
 /*@
   @ public behavior
   @
   @ pre factory != null;
   @
   @ post \result != null;
   @*/
	public abstract <W> SelectionController createControl(WidgetFactory<W> factory);
	
	/**
	 * Create a predicate based on the current state of the user interface element.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract UniversalPredicate<? super T, Nothing> predicate();
	
	/**
	 * Set the context for the selector. This may update the user interface
	 * control if the selection is context dependent. Otherwise, nothing is done.
	 * 
	 * @param context The new context for configuring the predicate.
	 */
	public abstract void setContext(Object context);
	
}
