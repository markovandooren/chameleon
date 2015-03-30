package org.aikodi.chameleon.core.event.stream;


/**
 * <p>A stream of events that originate from a source.</p>
 * 
 * @author Marko van Dooren
 */
public class BaseStream<C,S> extends AbstractEventStream<C,S> {

  private EventStreamCollection _collection;
  public BaseStream(EventStreamCollection collection) {
    _collection = collection;
  }

  @Override
  protected void activate() {
    _collection.startNotification();
  }

  @Override
  protected void deactivate() {
    _collection.deactivateBaseStream();
  }
   
 }