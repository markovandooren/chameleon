package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;

import be.kuleuven.cs.distrinet.chameleon.util.action.Action;

public class QueuePollingCallableFactory<T,E extends Exception> extends QueuePollingFactory<T,E> implements CallableFactory {

	public QueuePollingCallableFactory(Action<T,E> action, Queue<T> queue) {
		super(action,queue);
	}

	@Override
	public Callable<Object> createCallable() {
		return new QueuePollingCallable<T>(queue()) {
			
			@Override
			public void process(T t) throws E{
				action().perform(t);
			} 
		};
	}


}
