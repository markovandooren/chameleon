package be.kuleuven.cs.distrinet.chameleon.analysis;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.SafeAction;

public abstract class Analysis<E extends Element, R extends Result<R>> extends SafeAction<E> {

	public Analysis(Class<E> type) {
		super(type);
	}
	
	@Override
	public final void perform(E t) {
		R result = analyse(t);
		_result = result.and(_result);
	}
	
	/**
	 * Perform the analysis on the given element.
	 * 
	 * @param element The element to be analyzed.
	 */
	protected abstract R analyse(E e);
	
	/**
	 * 
	 * @return
	 */
	public R result() {
		return _result;
	}
	
	private R _result;

}
