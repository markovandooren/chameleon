package org.aikodi.chameleon.util.concurrent;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.aikodi.rejuse.action.Action;

public abstract class QueuePollingFactory<T,E extends Exception> {

	public QueuePollingFactory(Action<T,E> action, Queue<T> queue) {
		_action = action;
		_queue = new ArrayBlockingQueue<>(queue.size(), true, queue);
	}
	
	private Action<T,E> _action;
	private Queue<T> _queue;

	public Action<T,E> action() {
		return _action;
	}

	public QueuePollingFactory() {
		super();
	}

	public T poll() {
		return _queue.poll();
	}

}
