package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;

public class TopDown<T extends Element,E extends Exception> extends Sequence<T,E> {

	public TopDown(Walker<T,? extends E> walker) {
		super(walker, null);
		setSecond(new Recurse<T,E>(this));
	}

}
