package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

public abstract class UnsafeAction<T,E extends Exception> extends Action<T> {

	@Override
	public void perform(T t) throws E {
		actuallyPerform(t);
	}

	@Override
	protected abstract void actuallyPerform(T t) throws E;
	
}
