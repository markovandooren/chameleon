package org.aikodi.chameleon.core.event;

import org.aikodi.contract.Contracts;

/**
 * A class of events that indicate that a change happend in a source element.
 * 
 * @author Marko van Dooren
 *
 * @param <C> The type of the object describing the change.
 * @param <S> The type of the element that was changed.
 */
public class Event<C,S> {
  private C _change;
  private S _source;
  
  /**
   * Create a new event that indicates that the given change happend in
   * the given source element. 
   * 
   * @param change An object describing the change. The change cannot be null.
   * @param source The object that was changed. The object cannot be null.
   */
  public Event(C change, S source) {
    Contracts.notNull(change, "The change of an event cannot be null.");
    Contracts.notNull(source, "The source of an event cannot be null.");
    this._change = change;
    this._source = source;
  }
  
  /**
   * @return an object describing the change that triggered this event.
   */
  public C change() {
    return _change;
  }
  
  /**
   * @return the object that was changed.
   */
  public S source() {
    return _source;
  }
  
}