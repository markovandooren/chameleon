package org.aikodi.chameleon.core.event.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>An object for selecting a source of events.</p> 
 * 
 * <p>The selector enables and disables propagation of events based on whether 
 * actual listeners are attached to one of its streams.</p>
 *
 * <p>See {@link EventStream} for documentation about attaching listeners
 * and filtering events.</p>
 * 
 * @param <C> The type of the change objects.
 * @param <S> The type of the event source.
 * 
 * @author Marko van Dooren
 */
public abstract class EventSourceSelector<C,S> {

	/**
	 * A list of event stream that flow through this event stream collection.
	 */
  private List<EventStream<?,?>> _baseStreams = new ArrayList<>();

  /**
   * Deactivate the given base stream. This method is invoked
   * when the last listener of the stream is removed. 
   * If there are no more active stream,  
   * {{@link #stopNotification()} is called.
   * 
   * @param baseStream The base event stream to be removed.
   */
  protected void deactivate(EventStream<? extends C,? extends S> baseStream) {
    _baseStreams.remove(baseStream);
    if(_baseStreams.isEmpty()) {
      stopNotification();
      tearDown();
    }
  }
  
  /**
   * Activate the given event stream. This method is invoked when
   * a listener was added to the given stream, and there were no listeners
   * before.
   * 
   * @param stream The stream that is activated.
   */
  protected void activate(EventStream<? extends C,? extends S> stream) {
    _baseStreams.add(stream);
  }


  /**
   * Remove any state regarding this event stream collection.
   */
  protected abstract void tearDown();
  
  /**
   * Start notifying this event stream collection of events. This
   * method is invoked when there are no current listeners to any stream
   * in this collection, and a listener is added.
   */
  protected abstract void startNotification();

  /**
   * Stop notifying this event stream collection of events. This
   * method is invoked when no stream in this collection has any 
   * listeners left.
   */
  protected abstract void stopNotification();
  
  /**
   * Disconnect all listeners.
   */
  protected void disconnect() {
    while(!_baseStreams.isEmpty()) {
      _baseStreams.get(_baseStreams.size() - 1).disconnect();
    }
  }
}