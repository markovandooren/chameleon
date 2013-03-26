package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.concurrent.TimeUnit;

public abstract class FixedThreadRunnableExecutor extends FixedThreadExecutor {

	public FixedThreadRunnableExecutor(RunnableFactory factory) {
		_factory = factory;
	}
	
	public void run() throws InterruptedException, Exception {
		int availableProcessors = availableProcessors();
		for(int i=0; i<availableProcessors;i++) {
			executor().execute(factory().createRunnable());
		}
		executor().shutdown();
		executor().awaitTermination(100, TimeUnit.HOURS);
	}
	
	
	public RunnableFactory factory() {
		return _factory;
	}
	
	private RunnableFactory _factory;
	
	
}
