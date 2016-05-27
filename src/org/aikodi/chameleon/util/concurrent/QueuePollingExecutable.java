package org.aikodi.chameleon.util.concurrent;

public abstract class QueuePollingExecutable<T> {

	public QueuePollingExecutable(QueuePollingFactory<T,?> queue) {
		this._queue = queue;
	}

	private QueuePollingFactory<T,?> _queue;

	public T poll() {
		return _queue.poll();
	}

	public QueuePollingExecutable() {
		super();
	}

	public abstract void process(T t) throws Exception;

}
