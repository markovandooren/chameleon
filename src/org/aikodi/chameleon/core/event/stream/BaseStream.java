package org.aikodi.chameleon.core.event.stream;



/**
 * <p>A stream of events that originate from a source.</p>
 * 
 * @author Marko van Dooren
 */
public class BaseStream<C,S> extends AbstractEventStream<C,S> {

  private EventStreamCollection<? super C,? super S> _collection;
  /**
   * Initialize a new base stream for the given stream collection.
   * 
   * @param collection
   */
  public BaseStream(EventStreamCollection<? super C,? super S> collection) {
    _collection = collection;
  }

  @Override
  protected void activate() {
    _collection.activate(this);
    _collection.startNotification();
  }

  @Override
  protected void deactivate() {
    _collection.deactivateBaseStream(this);
  }
   
 }