package chameleon.util.concurrent;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.exception.ChameleonProgrammerException;

public class QueuePollingRunnableFactory<T> extends QueuePollingFactory<T> implements RunnableFactory {

	public QueuePollingRunnableFactory(SafeAction<T> action, Collection<T> collection) {
		this(action,new ArrayBlockingQueue<T>(collection.size(), true, collection));
	}
	
	public QueuePollingRunnableFactory(SafeAction<T> action, BlockingQueue<T> queue) {
		super(action,queue);
	}
	
	public SafeAction<T> action() {
		return (SafeAction<T>) super.action();
	}
	
	@Override
	public Runnable createRunnable() {
		return new QueuePollingRunnable<T>(queue()) {
			
			@Override
			public void process(T t) {
				action().perform(t);
			} 
		};
	}

}
