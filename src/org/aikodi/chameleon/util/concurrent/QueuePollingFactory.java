package org.aikodi.chameleon.util.concurrent;

import org.aikodi.rejuse.action.UniversalConsumer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class QueuePollingFactory<T,E extends Exception> {

	public QueuePollingFactory(UniversalConsumer<T,E> action, Queue<T> queue) {
		_action = action;
		_queue = new ArrayBlockingQueue<>(queue.size(), true, queue);
	}
	
	private UniversalConsumer<T,E> _action;
	private Queue<T> _queue;

	public UniversalConsumer<T,E> action() {
		return _action;
	}

	public QueuePollingFactory() {
		super();
	}

	public T poll() {
		return _queue.poll();
	}

}
