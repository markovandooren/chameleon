package be.kuleuven.cs.distrinet.chameleon.ui.widget;

public abstract class Selector<T> {

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
	 * Set the context for the selector. This may update the user interface
	 * control if the selection is context dependent. Otherwise, nothing is done.
	 * 
	 * @param context The new context for configuring the predicate.
	 */
	public abstract void setContext(Object context);
	

}
