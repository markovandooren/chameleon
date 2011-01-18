package chameleon.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FixedThreadCallableExecutor<E extends Exception> extends FixedThreadExecutor {
	public FixedThreadCallableExecutor(CallableFactory factory) {
		_factory = factory;
	}
	
	public void run() throws InterruptedException, E {
		int availableProcessors = availableProcessors();
		List<Future> futures = new ArrayList<Future>();
		try {
		for(int i=0; i<availableProcessors;i++) {
			Future fut = executor().submit(factory().createCallable());
			fut.get();
			futures.add(fut);
		}
		executor().shutdown();
		executor().awaitTermination(100, TimeUnit.HOURS);
		}
		catch(ExecutionException e) {
			throw (E)e.getCause();
		}
	}
	
	
	public CallableFactory factory() {
		return _factory;
	}
	
	private CallableFactory _factory;
	

}
