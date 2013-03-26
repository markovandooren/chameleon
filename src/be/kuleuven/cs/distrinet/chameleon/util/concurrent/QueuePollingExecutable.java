package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.Queue;

public abstract class QueuePollingExecutable<T> {

	public QueuePollingExecutable(Queue<T> queue) {
		this._queue = queue;
	}

	protected Queue<T> _queue;

	public Queue<T> queue() {
		return _queue;
	}

	public QueuePollingExecutable() {
		super();
	}

	public abstract void process(T t) throws Exception;

}
