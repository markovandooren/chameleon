package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A default implementation for event streams.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of the change object.
 * @param <S> The type of the sender of the event.
 */
public abstract class AbstractEventStream<C,S> implements EventStream<C,S> {
	
  private List<EventListener<? super C,? super S>> _listeners;

  /**
   * {@inheritDoc}
   */
  public void call(EventListener<? super C,? super S> listener) {
    if(listener == null) {
      throw new IllegalArgumentException("The event listener cannot be null.");
    }
    if(_listeners == null) {
      _listeners = new ArrayList<>();
      activate();
    }
    _listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void stopCalling(EventListener<? super C,? super S> callBack) {
    _listeners.remove(callBack);
    if(_listeners.isEmpty()) {
      _listeners = null;
      deactivate();
    }
  }

  /**
   * Send the given event to all listeners.
   * 
   * @param event The event to be sent. The event is not null.
   */
  public void send(Event<? extends C, ? extends S> event) {
  	if(event == null) {
  		throw new IllegalArgumentException("The event cannot be null.");
  	}
    for(EventListener<? super C,? super S> consumer: _listeners) {
      consumer.accept(event);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnect() {
    _listeners = null;
    deactivate();
  }

  /**
   * Activate this event stream. This method is called when a listener
   * is added, and there were no listeners.
   */
  protected abstract void activate();

  /**
   * Deactivate this event stream. This method is called when a listener
   * is removed, and no listeners remain.
   */
  protected abstract void deactivate();
}