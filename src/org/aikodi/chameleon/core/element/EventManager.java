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
    _element.enableChangeNotification();
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
    _descendantStream = null;
    _anyStream = null;
    _element.disableChangeNotification();
  }
  
  void notify(Event<? extends Change,? extends Element> event) {
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

  private EventStream<Change,Element> _descendantStream;
  public EventStream<Change,Element> descendant() {
    if(_descendantStream == null) {
      _descendantStream = new DescendantStream(_element);
    }
    return _descendantStream;
  }
  
  private EventStream<Change,Element> _anyStream;

  public EventStream<Change,Element> any() {
    if(_anyStream == null) {
      _anyStream = self().union(descendant());
    }
    return _anyStream;
  }
}