package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

public abstract class Action<T> {

	public void perform(T t) throws Exception {
		actuallyPerform(t);
	}
	
	protected abstract void actuallyPerform(T t) throws Exception;

}
