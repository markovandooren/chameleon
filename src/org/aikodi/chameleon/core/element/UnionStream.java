package org.aikodi.chameleon.core.element;

public class UnionStream<C,S> extends AbstractEventStream<C, S> implements EventListener<C, S> {

  private EventStream<? extends C, ? extends S> _first;
  private EventStream<? extends C, ? extends S> _second;
  
  public UnionStream(EventStream<? extends C, ? extends S> first, EventStream<? extends C, ? extends S> second) {
    this._first = first;
    this._second = second;
  }

  @Override
  protected void activate() {
    _first.call(this);
    _second.call(this);
  }

  @Override
  protected void deactivate() {
    _first.stopCalling(this);
    _second.stopCalling(this);
  }

  @Override
  public void accept(Event<? extends C, ? extends S> event) {
    send(event);
  }

}
