package be.kuleuven.cs.distrinet.chameleon.analysis;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.SafeAction;

public abstract class Analysis<E extends Element, R extends Result<R>> extends SafeAction<E> {

	public Analysis(Class<E> type) {
		super(type);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract R result();
}
