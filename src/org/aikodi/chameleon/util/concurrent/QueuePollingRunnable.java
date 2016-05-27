package org.aikodi.chameleon.util.concurrent;

public abstract class QueuePollingRunnable<T> extends QueuePollingExecutable<T> implements Runnable {

	public QueuePollingRunnable(QueuePollingFactory<T,?> queue) {
		super(queue);
	}
	
	@Override
	public final void run() {
		T t = poll();
		while(t != null) {
			process(t);
			t = poll();
		}

	}
	
	@Override
   public abstract void process(T t);

}
