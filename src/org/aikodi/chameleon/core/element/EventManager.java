package org.aikodi.chameleon.core.element;


public class EventManager {
  private final Element _element;

  public EventManager(Element element) {
    if(element == null) {
      throw new IllegalArgumentException("The given element cannot be null.");
    }
    this._element = element;
  }

//  private Set<EventStream<Change,Element>> _streams;

//  void activate(EventStream<Change,Element> stream) {
//    if(_streams == null) {
//      _streams = new HashSet<>(5);
//      activate();
//    }
//    _streams.add(stream);
//  }

  protected void activate() {
  }

//  void deactivate(EventStream<Change,Element> stream) {
//    _streams.remove(stream);
//    if(_streams.isEmpty()) {
//      _streams = null;
//      deactivate();
//    }
//  }

  protected void deactivate() {
    _baseStream = null;
  }
  
  void notify(Event<Change,Element> event) {
    if(_baseStream != null) {
      _baseStream.send(event);
    }
  }

  private BaseStream _baseStream;

  public EventStream<Change,Element> self() {
    if(_baseStream == null) {
      _baseStream = new BaseStream(this);
    }
    return _baseStream;
  }

  public EventStream<Change,Element> descendant() {
    return new DescendantStream(_element);
  }
  
  public EventStream<Change,Element> any() {
    return self().union(descendant());
  }
}