package chameleon.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;

public class QueuePollingCallableFactory<T,E extends Exception> extends QueuePollingFactory<T> implements CallableFactory {

//	public QueuePollingCallableFactory(UnsafeAction<T,E> action, Collection<T> collection) {
//		this(action,new ArrayBlockingQueue<T>(collection.size(), true, collection));
//	}
	
	public QueuePollingCallableFactory(UnsafeAction<T,E> action, Queue<T> queue) {
		super(action,queue);
	}

	public UnsafeAction<T,E> action() {
		return (UnsafeAction<T, E>) super.action();
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
