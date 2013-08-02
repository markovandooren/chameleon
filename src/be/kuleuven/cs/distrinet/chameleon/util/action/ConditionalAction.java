package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

public class ConditionalAction<T, E extends Exception> extends Action<T, E> {

	public ConditionalAction(Class<T> type, Predicate<T,? extends E> predicate, Action<T, ? extends E> action) {
		super(type);
		this._predicate = predicate;
		this._action = action;
	}

	private Predicate<T, ? extends E> _predicate;
	
	private Action<T,? extends E> _action;
	
	@Override
	protected void doPerform(T object) throws E {
		if(predicate().eval(object)) {
			action().perform((T)object);
		}
	}

	public Predicate<T, ? extends E> predicate() {
		return _predicate;
	}

	protected void setPredicate(Predicate<T, ? extends E> predicate) {
		_predicate = predicate;
	}

	public Action<T, ? extends E> action() {
		return _action;
	}

	public void setAction(Action<T,E> action) {
		_action = action;
	}

	
}
