package chameleon.util.concurrent;

import java.util.concurrent.BlockingQueue;

public abstract class QueuePollingExecutable<T> {

	public QueuePollingExecutable(BlockingQueue<T> queue) {
		this._queue = queue;
	}

	protected BlockingQueue<T> _queue;

	public BlockingQueue<T> queue() {
		return _queue;
	}

	public QueuePollingExecutable() {
		super();
	}

	public abstract void process(T t) throws Exception;

}