package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;


public class BaseStream extends AbstractEventStream<Change,Element> {

  private EventStreamCollection _manager;
  public BaseStream(EventStreamCollection manager) {
    _manager = manager;
  }

  @Override
  protected void activate() {
    _manager.startNotification();
  }

  @Override
  protected void deactivate() {
    _manager.deactivate();
  }
   
 }