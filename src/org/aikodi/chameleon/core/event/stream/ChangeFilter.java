package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;
import org.aikodi.rejuse.contract.Contracts;

/**
 * An event stream that filters events based on the type of change.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of changes propagated by this change filter.
 * @param <S> The type of the source elements of the propagated events.
 */
public class ChangeFilter<C,S> extends AbstractEventStream<C,S>  implements EventListener<Object,S> {

	/**
	 * The source stream from which the events are filtered.
	 * The source stream cannot be null.
	 */
  private EventStream<? super C, S> _sourceStream;
  
  /**
   * A class representing the type of changes propagates by this filter.
   * The change type is not null.
   */
  private Class<C> _changeType;

  /**
   * Create a new change filter that propagates changes of the given type
   * from the given source stream.
   * 
   * @param sourceStream The stream from which events are forwarded.
   *                     The source stream cannot be null.
   * @param changeType A class representing the types of changes that are forwarded.
   *                   The class cannot be null.
   */
  public ChangeFilter(EventStream<? super C, S> sourceStream, Class<C> changeType) {
  	Contracts.notNull(sourceStream, "The source stream cannot be null.");
  	Contracts.notNull(changeType, "The change type cannot be null.");
    _sourceStream = sourceStream;
    _changeType = changeType;
  }

  /**
   * @return A class representing the type of changes propagated by this
   *         change filter. The result is not null.
   */
  public Class<C> changeType() {
  	return _changeType;
  }
  
  /**
   * @return The stream from which events are forwarded.
   *         The result is not null.
   */
  public EventStream<? super C, S> sourceStream() {
  	return _sourceStream;
  }
  
  /**
   * Tell the source stream to call this event listener.
   */
  @Override
  protected void activate() {
    _sourceStream.call(this);
  }

  /**
   * Stop forwarding events from the source stream.
   * This unregisters this change filter as an event
   * listener.
   */
  @Override
  protected void deactivate() {
    _sourceStream.stopCalling(this);
  }

  /**
   * Forward the given event to the listeners of this stream
   * if its change is an instance of the change type of this
   * change filter, or our of its subclasses.
   */
  @Override
  public void accept(Event<? extends Object, ? extends S> event) {
    if(_changeType.isInstance(event.change())) {
      send((Event<? extends C, ? extends S>) event);
    }
  }


}
