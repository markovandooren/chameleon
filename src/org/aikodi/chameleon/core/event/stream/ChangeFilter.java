package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;

/**
 * An event stream that filters events based on the type of change.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of changes propagated by this change filter.
 * @param <S> The type of the source elements of the propagated events.
 */
public class ChangeFilter<C,S> extends AbstractEventStream<C,S>  implements EventListener<Object,S> {

  private EventStream<? super C, S> _parent;
  private Class<C> _change;

  public ChangeFilter(EventStream<? super C, S> parent, Class<C> changeType) {
    this._parent = parent;
    this._change = changeType;
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
  public void accept(Event<? extends Object, ? extends S> event) {
    if(_change.isInstance(event.change())) {
      send((Event<? extends C, ? extends S>) event);
    }
  }


}
