package org.aikodi.chameleon.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aikodi.chameleon.core.Config;

public class FixedThreadExecutor {

	public FixedThreadExecutor(ExecutorService executor) {
		_executor = executor;
		_availableProcessors = Runtime.getRuntime().availableProcessors();
	}
	public FixedThreadExecutor() {
		_availableProcessors = Runtime.getRuntime().availableProcessors();
		_executor = Executors.newFixedThreadPool(availableProcessors());
	}

	protected int _availableProcessors;

	public int availableProcessors() {
		boolean singleThreaded = Config.singleThreaded();
		if(singleThreaded) {
			return 1;
		} else {
			return _availableProcessors;
		}
	}

	protected ExecutorService _executor;

	public ExecutorService executor() {
		return _executor;
	}
	
}
