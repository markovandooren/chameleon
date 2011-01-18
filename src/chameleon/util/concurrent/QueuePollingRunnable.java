package chameleon.util.concurrent;

import java.util.Queue;

public abstract class QueuePollingRunnable<T> extends QueuePollingExecutable<T> implements Runnable {

	public QueuePollingRunnable(Queue<T> queue) {
		super(queue);
	}
	
	@Override
	public final void run() {
		Queue<T> queue = queue();
		T t = queue.poll();
		while(t != null) {
			process(t);
			t = queue.poll();
		}

	}
	
	public abstract void process(T t);

}
