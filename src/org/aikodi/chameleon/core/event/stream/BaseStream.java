package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;


public class BaseStream extends AbstractEventStream<Change,Element> {

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