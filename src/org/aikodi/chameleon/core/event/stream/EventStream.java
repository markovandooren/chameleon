package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.event.EventListener;

/**
 * <p>A class for implementing the observer pattern via
 * event streams that can be filtered and combined.</p>
 * 
 * <p>As described in more detail in {@link Event}, an event
 * consists of an object representing a change, and the sender
 * of the event.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of the change object.
 * @param <S> The type of the sender of the event.
 */
public interface EventStream<C, S> {

	/**
	 * <p>Register the given listener. Whenever an event is sent via
	 * this stream, the listener will be called.</p>
	 * 
	 * <p>If there were no listeners before, the stream will activate itself.</p>
	 * 
	 * @param listener The listener to be called. The listener
	 *                 cannot be null.
	 *                 
	 * @throws IllegalArgumentException The given listener is null.
	 */
  public void call(EventListener<? super C,? super S> listener);

  /**
   * <p>Unregister the given listener. After calling this method, the
   * given listener will no longer receive events from this stream.</p>
   * 
   * <p>If there are no listeners left, the stream will deactivate itself.</p>
   * 
   * @param listener The listener to be removed.
   */
  public void stopCalling(EventListener<? super C,? super S> listener);
  
  /**
   * <p>Create a new event stream that offers a filtered view on this event stream.
   * Only events of the given type are propagated. The advantage of the
   * filter is that the listener does not need to use the generic event type and
   * downcast to the desired event type.</p>
   * 
   * <p>Suppose you have an event type NameChanged. To listen only to name changes
   * that are sent via a stream, you can use the code below.</p>
   * 
   * <code>
   * stream.about(NameChanged.class).call(e -> ... e.change() ... ); // e.change() has type NameChanged.
   * </code>
   * 
   * @param changeType The class object representing the type of change that must
   *                   be propagated.
   *                   
   * @return A stream that propagates all events from this event stream whose change 
   *         is of the given change type.
   *         
   * @throws IllegalArgumentException The given class object is null.
   */
  public default <T extends C> EventStream<T,S> sends(Class<T> changeType) {
    if(changeType == null) {
      throw new IllegalArgumentException("The class object cannot be null.");
    }
    return new ChangeFilter<T, S>(this, changeType);
  }
  
  public default EventStream<C,S> sends() {
  	return this;
  }

  /**
   * <p>Create a new event stream that offers a filtered view on this event stream.
   * Only events that are from the given type of source objects are propagated. 
   * The advantage of the filter is that the listener does not need to use the more
   * general source type {@link S} and downcast to the desired source type.</p>
   * 
   * <p>Suppose you have an object of type Method. To listen only to changes
   * that are sent via this stream by a Method, you can use the code below.</p>
   * 
   * <code>
   * stream.from(Method.class).call(e -> ... e.source() ... ); // e.source() has type NameMethod.
   * </code>
   * 
   * @param sourceType The class object representing the type of source objects from
   *                   from which events must be propagated.
   *                   
   * @return A stream that propagates all events from this event stream whose source object 
   *         is of the given source type.
   *         
   * @throws IllegalArgumentException The given class object is null.
   */
  public default <T extends S> EventStream<C,T> from(Class<T> sourceType) {
    if(sourceType == null) {
      throw new IllegalArgumentException("The class object cannot be null.");
    }
    return new SourceFilter<C,T>(this, sourceType);
  }

  /**
   * Create an event stream that combines the events of this event stream and
   * the given other event stream.
   * 
   * @param other The event stream whose events must be propagated along with the
   *              events of this stream. The stream cannot be null.
   *              
   * @return An event stream that propagates all events coming from this event stream
   *         and the given other event stream.
   *         
   * @throws IllegalArgumentException The given other event stream is null.
   */
  public default EventStream<C,S> union(EventStream<? extends C, ? extends S> other) {
    return new UnionStream<C, S>(this,other);
  }
  
  /**
   * Disconnect all listeners and deactivate this stream.
   */
  public void disconnect();
}
