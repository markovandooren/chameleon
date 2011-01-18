package chameleon.util.concurrent;

import java.util.concurrent.BlockingQueue;

public class QueuePollingFactory<T> {

	public QueuePollingFactory(Action<T> action, BlockingQueue<T> queue) {
		_action = action;
		_queue = queue;
	}
	
	protected Action<T> _action;
	protected BlockingQueue<T> _queue;

	public Action<T> action() {
		return _action;
	}

	public QueuePollingFactory() {
		super();
	}

	public BlockingQueue<T> queue() {
		return _queue;
	}

}