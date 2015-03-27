package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;

public class DescendantStream extends AbstractEventStream<Change,Element> implements EventListener<Change,Element> {

  private Element _element;

  private EventListener<? super ChildAdded, ? super Element> _adder = c -> {
    c.change().element().when().any().call(this);
  };
  private EventListener<? super ChildRemoved, ? super Element> _remover = c -> {
    c.change().element().when().any().stopCalling(this);
  };

  private EventStream<ChildAdded, Element> _addStream;

  private EventStream<ChildRemoved, Element> _removeStream;

  public DescendantStream(Element element) {
    this._element = element;
  }

  @Override
  protected void activate() {
    _element.children().stream().forEach(c -> {
      c.when().any().call(this);
    });
    _addStream = _element.when().self().about(ChildAdded.class);
    _addStream.call(_adder);
    _removeStream = _element.when().self().about(ChildRemoved.class);
    _removeStream.call(_remover);
  }

  @Override
  protected void deactivate() {
    _element.children().stream().forEach(c -> {
      c.when().any().stopCalling(this);
    });
    _addStream.stopCalling(_adder);
    _removeStream.stopCalling(_remover);
  }

  @Override
  public void accept(Event<? extends Change, ? extends Element> event) {
    send(event);
  }

}
