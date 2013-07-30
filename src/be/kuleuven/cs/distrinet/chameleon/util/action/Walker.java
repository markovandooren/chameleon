package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public abstract class Walker<E extends Exception> extends Action<Element,E> {

	public Walker() {
		super(Element.class);
	}

}
