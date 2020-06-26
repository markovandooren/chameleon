package org.aikodi.chameleon.core.event.stream;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementEventSourceSelector;
import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.EventListener;
import org.aikodi.chameleon.core.event.association.ChildAdded;
import org.aikodi.chameleon.core.event.association.ChildRemoved;
import org.aikodi.rejuse.contract.Contracts;

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

	/**
	 * A call-back listener to register this stream as an event listener to
	 * children that are added to the element of the event stream collection.
	 */
  private EventListener<? super ChildAdded, ? super Element> _childAddedListener = addedChild -> {
    addedChild.change().element().when().any().sends().call(this);
  };

	/**
	 * A call-back listener to unregister this stream as an event listener from
	 * children that are removed from the element of the event stream collection.
	 */
  private EventListener<? super ChildRemoved, ? super Element> _childRemovedListener = removedChild -> {
    removedChild.change().element().when().any().sends().stopCalling(this);
  };

  private EventStream<ChildAdded, Element> _addStream;
  private EventStream<ChildRemoved, Element> _removeStream;
  private ElementEventSourceSelector _collection;

  /**
   * Create a new stream that gathers the events of the descendants of the
   * element of the given event stream collection.
   * 
   * @param collection The event stream collection of the nearest common 
   *                   ancestor of the elements from which the events must be 
   *                   propagated. The collection cannot be null.
   */
  public DescendantStream(ElementEventSourceSelector collection) {
  	Contracts.notNull(collection, "The event stream collection cannot be null.");
    this._collection = collection;
  }
  
  /**
   * @return The nearest common ancestor of the elements from which the events
   *         must be propagated. The result is not null.
   */
  public Element element() {
    return _collection.element();
  }

  /**
   * {@inheritDoc}
   * 
   * <p>This stream is registered as an event listener to the 
   * {@link EventSourceSelector#any()} stream of every child of the 
   * {@link #element()} of this stream.</p>
   * 
   * <p>In addition, listeners are registered to the 
   * {@link EventSourceSelector#self()} stream of the {@link #element()} of 
   * this stream in order to attach this stream to new children, and remove
   * this stream from removed children.
   */
  @Override
  protected void activate() {
    element().lexical().children().stream().forEach(c -> {
      c.when().any().sends().call(this);
    });
    _addStream = element().when().self().sends(ChildAdded.class);
    _addStream.call(_childAddedListener);
    _removeStream = element().when().self().sends(ChildRemoved.class);
    _removeStream.call(_childRemovedListener);
  }

  /**
   * {@inheritDoc}
   * <p>This stream is unregistered as an event listener to the 
   * {@link EventSourceSelector#any()} stream of every child of the 
   * {@link #element()} of this stream.</p>
   * 
   * <p>In addition, the listeners for adding this stream to added children
   * and removing it from removed children are unregistered.
   */
  @Override
  protected void deactivate() {
    element().lexical().children().stream().forEach(c -> {
      c.when().any().sends().stopCalling(this);
    });
    _addStream.stopCalling(_childAddedListener);
    _removeStream.stopCalling(_childRemovedListener);
    _collection.deactivate(this);
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
