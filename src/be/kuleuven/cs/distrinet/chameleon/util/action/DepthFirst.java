package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Action;


public class DepthFirst<T extends Element, E extends Exception> extends Sequence<T,E> {

	public DepthFirst(Walker<T, ? extends E> walker) {
		super(walker.type(),null, walker);
		setFirst(new Recurse<T,E>(walker.type(),this));
	}
	
	public DepthFirst(Class<T> type, Action<? super T,? extends E> action) {
		super(type,null, action);
		setFirst(new Recurse<T,E>(type,this));
	}

}
