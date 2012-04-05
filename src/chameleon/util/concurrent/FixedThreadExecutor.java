package chameleon.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadExecutor {

	public FixedThreadExecutor() {
		_availableProcessors = Runtime.getRuntime().availableProcessors();
		_executor = Executors.newFixedThreadPool(availableProcessors());
	}

	protected int _availableProcessors;

	public int availableProcessors() {
		if(SINGLE_THREADED) {return 1;}
		return _availableProcessors;
	}

	protected ExecutorService _executor;

	public ExecutorService executor() {
		return _executor;
	}
	
	private final static boolean SINGLE_THREADED=true;

}