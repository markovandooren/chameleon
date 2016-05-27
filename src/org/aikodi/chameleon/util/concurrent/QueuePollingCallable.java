package org.aikodi.chameleon.util.concurrent;

import java.util.concurrent.Callable;

public abstract class QueuePollingCallable<T>  extends QueuePollingExecutable<T> implements Callable<Object> {

	public QueuePollingCallable(QueuePollingFactory<T,?> queue) {
		super(queue);
	}
	
	@Override
	public Object call() throws Exception {
		T t = poll();
		while(t != null) {
			process(t);
			t = poll();
		}
		return null;
	}


}
