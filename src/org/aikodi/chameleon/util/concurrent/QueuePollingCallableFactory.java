package org.aikodi.chameleon.util.concurrent;

import org.aikodi.rejuse.action.UniversalConsumer;

import java.util.Queue;
import java.util.concurrent.Callable;

public class QueuePollingCallableFactory<T,E extends Exception> extends QueuePollingFactory<T,E> implements CallableFactory {

	public QueuePollingCallableFactory(UniversalConsumer<T,E> action, Queue<T> queue) {
		super(action,queue);
	}

	@Override
	public Callable<Object> createCallable() {
		return new QueuePollingCallable<T>(this) {
			
			@Override
			public void process(T t) throws E{
				action().perform(t);
			} 
		};
	}


}
