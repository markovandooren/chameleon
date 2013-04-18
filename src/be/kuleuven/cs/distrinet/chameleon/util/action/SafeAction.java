package be.kuleuven.cs.distrinet.chameleon.util.action;

/**
 * A type of actions that will not throw any exception.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the objects on which the action can operate.
 */
public abstract class SafeAction<T> extends Action<T, Nothing>{

	public SafeAction(Class<T> type) {
		super(type);
	}

}
