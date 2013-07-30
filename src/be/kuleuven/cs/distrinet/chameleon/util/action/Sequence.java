package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class Sequence<E extends Exception> extends Walker<E> {

	public Sequence(Action<? super Element, ? extends E> first, Action<? super Element, ? extends E> second) {
		_first = first;
		_second = second;
	}

	private Action<? super Element, ? extends E> _first;
	
	public Action<? super Element, ? extends E> first() {
		return _first;
	}

	protected void setFirst(Walker<? extends E> first) {
		_first = first;
	}

	public Action<? super Element, ? extends E> second() {
		return _second;
	}

	protected void setSecond(Action<? super Element, ? extends E> second) {
		_second = second;
	}

	private Action<? super Element, ? extends E> _second;
	
	@Override
	protected void doPerform(Element element) throws E {
		first().perform(element);
		second().perform(element);
	}

}
