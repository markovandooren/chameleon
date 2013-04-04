package be.kuleuven.cs.distrinet.chameleon.util.concurrent;

/**
 * A class of actions.
 * @author Marko van Dooren
 *
 * @param <T>
 */
public abstract class Action<T> {

	public void perform(T t) throws Exception {
		actuallyPerform(t);
	}
	
	protected abstract void actuallyPerform(T t) throws Exception;

}
