package chameleon.util.concurrent;

import java.util.concurrent.Callable;

public interface CallableFactory {

	public Callable createCallable();
}
