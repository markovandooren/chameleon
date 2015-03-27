package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;

/**
 * <p>A collection of event streams for an element.</p> 
 * 
 * <p>The collection enables and disables propagation of events based on whether 
 * actual listeners are attached to a stream in its collection. Available 
 * streams are:</p>
 * <ol>
 *   <li>{@link #self()} sends events that originate from the {@link #element()}
 *   of this event stream collection.</li>
 *   <li>{@link #descendant()} sends events that originate from a descendant
 *   of the {@link #element()} of this event stream collection.</li>
 *   <li>{@link #any()} sends events that originate from either {@link #self()}
 *   or {@link #descendant()}</li>
 * </ol>
 * 
 * <p>See {@link EventStream} for documentation about attaching listeners
 * and filtering events.</p>
 * 
 * @author Marko van Dooren
 */
public abstract class EventStreamCollection {

  private BaseStream _baseStream;
  private EventStream<Change,Element> _descendantStream;
  private EventStream<Change,Element> _anyStream;

  /**
   * Deactivate this event stream collection. This method is invoked
   * when the last listener is removed. All stream caches are cleared, and
   * {{@link #stopNotification()} is called.
   */
  protected void deactivateBaseStream() {
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
  
  /**
   * Start notifying this event stream collection of events. This
   * method is invoked when there are no current listeners to any stream
   * in this collection, and a listener is added.
   */
  protected abstract void startNotification();

  /**
   * Stop notifying this event stream collection of events. This
   * method is invoked when no stream in this collection has any 
   * listeners left.
   */
  protected abstract void stopNotification();
  
  /**
   * @return the element from which the events are gathered.
   */
  public abstract Element element();
  
  /**
   * Propagate the given event to the {{@link #self()} stream of this collection.
   * 
   * @param event The event to be sent.
   */
  public void notify(Event<? extends Change,? extends Element> event) {
    if(_baseStream != null) {
      if(event == null) {
        throw new IllegalArgumentException("An event cannot be null.");
      }
      _baseStream.send(event);
    }
  }

  /**
   * @return A stream the send event that originate from the {@link #element()}
   * of this event stream collection.
   */
  public EventStream<Change,Element> self() {
    if(_baseStream == null) {
      _baseStream = new BaseStream(this);
    }
    return _baseStream;
  }

  /**
   * @return A stream the send event that originate from the descendants of
   * the {@link #element()} of this event stream collection.
   */
  public EventStream<Change,Element> descendant() {
    if(_descendantStream == null) {
      _descendantStream = new DescendantStream(this);
    }
    return _descendantStream;
  }
  
  /**
   * @return An event stream that combines the events of {@link #self()} and
   * {@link #descendant()}.
   */
  public EventStream<Change,Element> any() {
    if(_anyStream == null) {
      _anyStream = self().union(descendant());
    }
    return _anyStream;
  }
}