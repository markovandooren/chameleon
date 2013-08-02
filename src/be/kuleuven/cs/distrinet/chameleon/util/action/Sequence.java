package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class Sequence<T,E extends Exception> extends Walker<T,E> {

	public Sequence(Class<T> type, Action<? super T, ? extends E> first, Action<? super T, ? extends E> second) {
		super(type);
		_first = first;
		_second = second;
	}

	public Sequence(Walker<T, ? extends E> first, Action<? super T, ? extends E> second) {
		super(first.type());
		_first = first;
		_second = second;
	}

	private Action<? super T, ? extends E> _first;
	
	public Action<? super T, ? extends E> first() {
		return _first;
	}

	protected void setFirst(Action<? super T, ? extends E> first) {
		_first = first;
	}

	public Action<? super T, ? extends E> second() {
		return _second;
	}

	protected void setSecond(Action<? super T, ? extends E> second) {
		_second = second;
	}

	private Action<? super T, ? extends E> _second;
	
	@Override
	protected void doPerform(T element) throws E {
		first().perform(element);
		second().perform(element);
	}

}
