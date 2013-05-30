package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.Queue;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class QueuePollingFactory<T,E extends Exception> {

	public QueuePollingFactory(Action<T,E> action, Queue<T> queue) {
		_action = action;
		_queue = queue;
	}
	
	protected Action<T,E> _action;
	protected Queue<T> _queue;

	public Action<T,E> action() {
		return _action;
	}

	public QueuePollingFactory() {
		super();
	}

	public Queue<T> queue() {
		return _queue;
	}

}
