package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

import java.util.concurrent.Callable;

public interface CallableFactory {

	public Callable createCallable();
}
