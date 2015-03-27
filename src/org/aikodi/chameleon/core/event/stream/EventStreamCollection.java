package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;

/**
 * A class to manage the event streams of an element. It enables and disables
 * itself based on whether actual listeners are attached.
 * 
 * @author Marko van Dooren
 */
public abstract class EventStreamCollection {

  protected abstract void startNotification();

  protected void deactivate() {
    _baseStream = null;
    _anyStream = null;
    stopNotification();
    cleanup();
  }

  protected void deactivateDescendantStream() {
    _descendantStream = null;
    cleanup();
  }
  
  private void cleanup() {
    if(_baseStream == null && _descendantStream == null) {
      clearEventStreamCollection();
    }
  }

  protected abstract void clearEventStreamCollection();
  protected abstract void stopNotification();
  
  protected abstract Element element();
  
  public void notify(Event<? extends Change,? extends Element> event) {
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
      _descendantStream = new DescendantStream(element());
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