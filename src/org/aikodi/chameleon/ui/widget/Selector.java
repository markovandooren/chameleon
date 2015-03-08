package org.aikodi.chameleon.ui.widget;


public interface Selector {

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
	public <W> SelectionController<? extends W> createControl(WidgetFactory<W> factory);
	
	/**
	 * Set the context for the selector. This may update the user interface
	 * control if the selection is context dependent. Otherwise, nothing is done.
	 * 
	 * @param context The new context for configuring the predicate.
	 */
	public void setContext(Object context);
	

}
