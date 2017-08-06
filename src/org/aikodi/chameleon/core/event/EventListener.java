package org.aikodi.chameleon.core.event;

/**
 * An interface for event listeners.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of changes that the listener listens to.
 * @param <S> The type of the sources that the listener listens to.
 */
public interface EventListener<C,S> {
  
	/**
	 * Accept the given event.
	 * 
	 * @param event The event that represents a change in a source.
	 */
 /*@
   @ pre event != null;
   @*/
  public void accept(Event<? extends C,? extends S> event);
}
