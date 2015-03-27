package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;

/**
 * <p>A stream that collects events from all descendants of its {@link #element()}</p>
 * 
 * <p>This stream coordinates the communication between event streams. It attaches
 * itself to new children added to its {@link #element()}, and removes itself
 * from children that are removed from its {@link #element()}.</p>
 * @author Marko van Dooren
 *
 */
public class DescendantStream extends AbstractEventStream<Change,Element> implements EventListener<Change,Element> {

  private EventListener<? super ChildAdded, ? super Element> _adder = c -> {
    c.change().element().when().any().call(this);
  };
  private EventListener<? super ChildRemoved, ? super Element> _remover = c -> {
    c.change().element().when().any().stopCalling(this);
  };

  private EventStream<ChildAdded, Element> _addStream;
  private EventStream<ChildRemoved, Element> _removeStream;
  private EventStreamCollection _collection;

  /**
   * Create a new stream that gathers the events of the descendants of the
   * element of the given event stream collection.
   * 
   * @param collection The event stream collection of the nearest common 
   *                   ancestor of the elements from which the events must be 
   *                   propagated.
   */
  public DescendantStream(EventStreamCollection collection) {
    this._collection = collection;
  }
  
  /**
   * @return The nearest common ancestor of the elements from which the events
   *         must be propagated.
   */
  public Element element() {
    return _collection.element();
  }

  /**
   * {@inheritDoc}
   * 
   * <p>This stream is registered as an event listener to the 
   * {@link EventStreamCollection#any()} stream of every child of the 
   * {@link #element()} of this stream.</p>
   * 
   * <p>In addition, listeners are registered to the 
   * {@link EventStreamCollection#self()} stream of the {@link #element()} of 
   * this stream in order to attach this stream to new children, and remove
   * this stream from removed children.
   */
  @Override
  protected void activate() {
    element().children().stream().forEach(c -> {
      c.when().any().call(this);
    });
    _addStream = element().when().self().about(ChildAdded.class);
    _addStream.call(_adder);
    _removeStream = element().when().self().about(ChildRemoved.class);
    _removeStream.call(_remover);
  }

  /**
   * {@inheritDoc}
   * <p>This stream is unregistered as an event listener to the 
   * {@link EventStreamCollection#any()} stream of every child of the 
   * {@link #element()} of this stream.</p>
   * 
   * <p>In addition, the listeners for adding this stream to added children
   * and removing it from removed children are unregistered.
   */
  @Override
  protected void deactivate() {
    element().children().stream().forEach(c -> {
      c.when().any().stopCalling(this);
    });
    _addStream.stopCalling(_adder);
    _removeStream.stopCalling(_remover);
    _collection.deactivateDescendantStream();
  }

  /**
   * {@inheritDoc}
   * 
   * The event is propagated via {@link #send(Event)}.
   */
  @Override
  public void accept(Event<? extends Change, ? extends Element> event) {
    send(event);
  }

}
