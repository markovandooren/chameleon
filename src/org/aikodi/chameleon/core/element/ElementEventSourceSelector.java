package org.aikodi.chameleon.core.element;

import org.aikodi.chameleon.core.event.Change;
import org.aikodi.chameleon.core.event.Event;
import org.aikodi.chameleon.core.event.stream.BaseStream;
import org.aikodi.chameleon.core.event.stream.DescendantStream;
import org.aikodi.chameleon.core.event.stream.EventSourceSelector;
import org.aikodi.chameleon.core.event.stream.EventStream;

/**
 * <p>
 * An event stream collection for Chameleon elements. This event stream
 * collection offers streams for listening to events from various sources.
 * </p>
 * 
 * <p>
 * Available streams are:
 * </p>
 * <ol>
 * <li>{@link #self()} sends events that originate from the {@link #element()}
 * of this event stream collection.</li>
 * <li>{@link #descendant()} sends events that originate from a descendant of
 * the {@link #element()} of this event stream collection.</li>
 * <li>{@link #any()} sends events that originate from either {@link #self()} or
 * {@link #descendant()}</li>
 * </ol>
 * 
 * @author Marko van Dooren
 */
public abstract class ElementEventSourceSelector extends EventSourceSelector<Change, Element> {

	private BaseStream<Change, Element> _baseStream;
	private EventStream<Change, Element> _descendantStream;
	private EventStream<Change, Element> _anyStream;

	/**
	 * @return A stream the send event that originate from the {@link #element()} of
	 *         this event stream collection. The result is not null.
	 */
	public EventStream<Change, Element> self() {
		if (_baseStream == null) {
			_baseStream = new BaseStream<Change, Element>(this);
		}
		return _baseStream;
	}

	/**
	 * @return A stream the send event that originate from the descendants of the
	 *         {@link #element()} of this event stream collection. The result is not
	 *         null.
	 */
	public EventStream<Change, Element> descendant() {
		if (_descendantStream == null) {
			_descendantStream = new DescendantStream(this);
		}
		return _descendantStream;
	}

	/**
	 * @return An event stream that combines the events of {@link #self()} and
	 *         {@link #descendant()}. The result is not null.
	 */
	public EventStream<Change, Element> any() {
		if (_anyStream == null) {
			_anyStream = self().union(descendant());
		}
		return _anyStream;
	}

	/**
	 * Propagate the given event to the {{@link #self()} stream of this collection.
	 * 
	 * @param event
	 *            The event to be sent.
	 */
	protected void notify(Event<? extends Change, ? extends Element> event) {
		if (_baseStream != null) {
			if (event == null) {
				throw new IllegalArgumentException("An event cannot be null.");
			}
			_baseStream.send(event);
		}
	}

	/**
	 * @return the element of this event stream collection.
	 */
	public abstract Element element();

	/**
	 * {@inheritDoc}
	 * 
	 * This override only serves to make the method available within
	 * this package. It performs a super call to do the actual work.
	 */
	@Override
	protected void disconnect() {
		super.disconnect();
	}

}