package chameleon.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public abstract class QueuePollingCallable<T>  extends QueuePollingExecutable<T> implements Callable<Object> {

	public QueuePollingCallable(BlockingQueue<T> queue) {
		super(queue);
	}
	
	@Override
	public Object call() throws Exception {
		BlockingQueue<T> queue = queue();
		T t = queue.poll();
		while(t != null) {
			process(t);
			t = queue.poll();
		}
		return null;
	}


}
