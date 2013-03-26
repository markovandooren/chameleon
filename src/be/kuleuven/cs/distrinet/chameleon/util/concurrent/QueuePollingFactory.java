package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.Queue;

public class QueuePollingFactory<T> {

	public QueuePollingFactory(Action<T> action, Queue<T> queue) {
		_action = action;
		_queue = queue;
	}
	
	protected Action<T> _action;
	protected Queue<T> _queue;

	public Action<T> action() {
		return _action;
	}

	public QueuePollingFactory() {
		super();
	}

	public Queue<T> queue() {
		return _queue;
	}

}
