package be.kuleuven.cs.distrinet.chameleon.analysis;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.util.action.TreeAction;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;

public abstract class Analysis<E extends Element, R extends Result<R>> extends TreeAction<E,Nothing> {

	public Analysis(Class<E> type, R initial) {
		super(type);
		setResult(initial);
	}
	
	/**
	 * 
	 * @return
	 */
	public final R result() {
		return _result;
	}
	
	protected void setResult(R result) {
		_result = result;
	}
	
	private R _result;
}
