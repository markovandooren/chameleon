package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;

public abstract class QueuePollingCallable<T>  extends QueuePollingExecutable<T> implements Callable<Object> {

	public QueuePollingCallable(Queue<T> queue) {
		super(queue);
	}
	
	@Override
	public Object call() throws Exception {
		Queue<T> queue = queue();
		T t = queue.poll();
		while(t != null) {
			process(t);
			t = queue.poll();
		}
		return null;
	}


}
