package org.aikodi.chameleon.core.element;

public class DescendantStream extends AbstractEventStream<Change,Element> implements EventListener<Change,Element> {

  private Element _element;

  public DescendantStream(Element element) {
    this._element = element;
  }

  @Override
  protected void activate() {
    _element.children().stream().forEach(c -> {
      EventStream<Change,Element> descendant = c.when().descendant();
      descendant.call(this);
    });
  }

  @Override
  protected void deactivate() {
    _element.children().stream().forEach(c -> {
      c.when().descendant().stopCalling(this);
    });
  }

  @Override
  public void accept(Event<? extends Change, ? extends Element> event) {
    send(event);
  }

}
