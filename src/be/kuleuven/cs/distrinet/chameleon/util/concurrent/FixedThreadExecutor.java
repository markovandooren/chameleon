package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.kuleuven.cs.distrinet.chameleon.core.Config;

public class FixedThreadExecutor {

	public FixedThreadExecutor() {
		_availableProcessors = Runtime.getRuntime().availableProcessors();
		_executor = Executors.newFixedThreadPool(availableProcessors());
	}

	protected int _availableProcessors;

	public int availableProcessors() {
		if(Config.singleThreaded()) {return 1;}
		return _availableProcessors;
	}

	protected ExecutorService _executor;

	public ExecutorService executor() {
		return _executor;
	}
	
}
