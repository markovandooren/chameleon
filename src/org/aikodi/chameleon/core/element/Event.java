package org.aikodi.chameleon.core.element;

public class Event<C,S> {
  private C _change;
  private S _source;
  
  public Event(C change, S source) {
    this._change = change;
    this._source = source;
  }
  
  public C change() {
    return _change;
  }
  
  public S source() {
    return _source;
  }
  
}