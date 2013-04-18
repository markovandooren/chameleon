package be.kuleuven.cs.distrinet.chameleon.util.action;

/**
 * An class for actions.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the element on which the action operates
 * @param <E> The type of exceptions that can be thrown by the action
 */
public abstract class Action<T, E extends Exception> {

	/**
	 * Create a new action that operates on items of the 
	 * given type.
	 * 
	 * @param type A {@link Class} object that represents the type
	 *             of objects on which the new action can operate.
	 */
	public Action(Class<T> type) {
		_type = type;
	}
	
	/**
	 * Perform the action.
	 * 
	 * @param object The object on which the action must be performed.
	 * @throws E
	 */
	public abstract void perform(T object) throws E;
	
	/**
	 * Return a class object that represents the type of
	 * objects on which this action can operate.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Class<T> type() {
		return _type;
	}
	
	private Class<T> _type;
}
