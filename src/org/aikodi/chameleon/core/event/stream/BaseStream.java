package org.aikodi.chameleon.core.event.stream;

import org.aikodi.rejuse.contract.Contracts;

/**
 * <p>A stream of events that originate from a source.</p>
 * 
 * @author Marko van Dooren
 */
public class BaseStream<C,S> extends AbstractEventStream<C,S> {

	/**
	 * The collection of stream 
	 */
  private EventSourceSelector<? super C,? super S> _collection;
  
  /**
   * Initialize a new base stream for the given stream collection.
   * 
   * @param collection
   */
  public BaseStream(EventSourceSelector<? super C,? super S> collection) {
  	Contracts.notNull(collection, "The event stream collection cannot be null.");
    _collection = collection;
  }

  /**
   * {@inheritDoc}
   * 
   * Tells the event stream collection to activate this base stream and gives it the
   * trigger to start notification of events.
   */
  @Override
  protected void activate() {
    _collection.activate(this);
    _collection.startNotification();
  }

  /**
   * {@inheritDoc}
   * 
   * Tells the event stream collection to deactivate this base stream.
   */
  @Override
  protected void deactivate() {
    _collection.deactivate(this);
  }
   
 }