package org.aikodi.chameleon.core.element;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventStream<C,S> implements EventStream<C,S> {
  private List<EventListener<? super C,? super S>> _consumers;

  public void call(EventListener<? super C,? super S> callBack) {
    if(callBack == null) {
      throw new IllegalArgumentException("The event callback handler cannot be null.");
    }
    if(_consumers == null) {
      _consumers = new ArrayList<>();
      activate();
    }
    _consumers.add(callBack);
  }

  public void stopCalling(EventListener<? super C,? super S> callBack) {
    _consumers.remove(callBack);
    if(_consumers.isEmpty()) {
      _consumers = null;
      deactivate();
    }
  }

  public void send(Event<? extends C, ? extends S> event) {
    for(EventListener<? super C,? super S> consumer: _consumers) {
      consumer.accept(event);
    }
  }

  protected abstract void activate();

  protected abstract void deactivate();
}