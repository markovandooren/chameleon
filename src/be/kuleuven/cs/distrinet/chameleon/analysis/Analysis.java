package be.kuleuven.cs.distrinet.chameleon.analysis;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.util.action.Walker;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;

public abstract class Analysis<E extends Element, R extends Result<R>> extends Walker<E,Nothing> {

	public Analysis(Class<E> type) {
		super(type);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract R result();
}
