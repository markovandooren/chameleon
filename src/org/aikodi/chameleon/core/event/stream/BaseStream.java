package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;


public class BaseStream extends AbstractEventStream<Change,Element> {

  private EventManager _manager;
  public BaseStream(EventManager manager) {
    _manager = manager;
  }

  @Override
  protected void activate() {
    _manager.activate();
  }

  @Override
  protected void deactivate() {
    _manager.deactivate();
  }
   
 }