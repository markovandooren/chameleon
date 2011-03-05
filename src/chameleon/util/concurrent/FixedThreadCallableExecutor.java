package chameleon.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FixedThreadCallableExecutor<E extends Exception> extends FixedThreadExecutor {
	public FixedThreadCallableExecutor(CallableFactory factory) {
		_factory = factory;
	}
	
	public void run() throws InterruptedException, E, ExecutionException {
		int availableProcessors = availableProcessors();
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
		for(int i=0; i<availableProcessors;i++) {
			tasks.add(factory().createCallable());
		}
		List<Future<Object>> futures = executor().invokeAll(tasks);
		for(int i=0; i<availableProcessors;i++) {
			try {
				futures.get(i).get();
			} catch (ExecutionException e) {
//				e.getCause().printStackTrace();
				throw e;
			}
		}
		executor().shutdown();
//		executor().awaitTermination();
	}
	
	
	public CallableFactory factory() {
		return _factory;
	}
	
	private CallableFactory _factory;
	

}
