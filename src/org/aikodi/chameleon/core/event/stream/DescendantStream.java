package org.aikodi.chameleon.core.element;

public class DescendantStream extends AbstractEventStream<Change,Element> implements EventListener<Change,Element> {

  private Element _element;

  private EventListener<? super Added, ? super Element> _adder = c -> {
    c.change().element().when().any().call(this);
  };
  private EventListener<? super Removed, ? super Element> _remover = c -> {
    c.change().element().when().any().stopCalling(this);
  };

  private EventStream<Added, Element> _addStream;

  private EventStream<Removed, Element> _removeStream;

  public DescendantStream(Element element) {
    this._element = element;
  }

  @Override
  protected void activate() {
    _element.children().stream().forEach(c -> {
      c.when().any().call(this);
    });
    _addStream = _element.when().self().about(Added.class);
    _addStream.call(_adder);
    _removeStream = _element.when().self().about(Removed.class);
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
