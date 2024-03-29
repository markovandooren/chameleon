package org.aikodi.chameleon.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadExecutor {

	public FixedThreadExecutor(ExecutorService executor) {
		_executor = executor;
		_availableProcessors = Runtime.getRuntime().availableProcessors();
	}
	public FixedThreadExecutor() {
		_availableProcessors = Runtime.getRuntime().availableProcessors();
		_executor = Executors.newFixedThreadPool(availableProcessors());
	}

	private int _availableProcessors;

	public int availableProcessors() {
		return _availableProcessors;
	}

	private ExecutorService _executor;

	public ExecutorService executor() {
		return _executor;
	}
	
}
