package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.util.StackOverflowTracer;



public class Sequence<T,E extends Exception> extends Walker<T,E> {
	
	StackOverflowTracer _tracer = new StackOverflowTracer(100);

	public Sequence(Walker<T, ? extends E> first, Walker<? super T, ? extends E> second) {
		super(first.type());
		_first = first;
		_second = second;
	}

	public Sequence(Class<T> type, Walker<T, ? extends E> first, Walker<? super T, ? extends E> second) {
		super(type);
		_first = first;
		_second = second;
	}

	private Walker<? super T, ? extends E> _first;
	
	public Walker<? super T, ? extends E> first() {
		return _first;
	}

	protected void setFirst(Walker<? super T, ? extends E> first) {
		_first = first;
	}

	public Walker<? super T, ? extends E> second() {
		return _second;
	}

	protected void setSecond(Walker<? super T, ? extends E> second) {
		_second = second;
	}

	private Walker<? super T, ? extends E> _second;
	
	@Override
	protected void doPerform(T element) throws E {
		first().enter(element);
		first().perform(element);
		second().enter(element);
		second().perform(element);
		second().exit(element);
		first().exit(element);
	}

}
