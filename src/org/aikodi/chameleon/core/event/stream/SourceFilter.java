package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;

public class SourceFilter<C,S> extends AbstractEventStream<C,S>  implements EventListener<C,Object> {

  private EventStream<C, ? super S> _parent;
  private Class<S> _source;

  public SourceFilter(EventStream<C, ? super S> parent, Class<S> source) {
    this._parent = parent;
    this._source = source;
  }

  @Override
  protected void activate() {
    _parent.call(this);
  }

  @Override
  protected void deactivate() {
    _parent.stopCalling(this);
  }

  @Override
  public void accept(Event<? extends C, ? extends Object> event) {
    if(_source.isInstance(event.source())) {
      send((Event<? extends C, ? extends S>) event);
    }
  }


}
