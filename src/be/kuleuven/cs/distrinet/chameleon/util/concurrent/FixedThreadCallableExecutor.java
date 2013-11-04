package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FixedThreadCallableExecutor<E extends Exception> extends FixedThreadExecutor {
	public FixedThreadCallableExecutor(CallableFactory factory) {
		_factory = factory;
	}

	public FixedThreadCallableExecutor(CallableFactory factory, ExecutorService service) {
		super(service);
		_factory = factory;
	}
	
	public void run() throws InterruptedException, E, ExecutionException {
		int availableProcessors = availableProcessors();
//		System.out.println("Using "+availableProcessors+" threads.");
		List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(availableProcessors);
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
//		executor().shutdown();
//		executor().awaitTermination();
	}
	
	
	public CallableFactory factory() {
		return _factory;
	}
	
	private CallableFactory _factory;
	

}
