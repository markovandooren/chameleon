package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;


public class DepthFirst<T extends Element, E extends Exception> extends Sequence<T,E> {

	public DepthFirst(TreeAction<T, ? extends E> walker) {
		super(walker.type(),null, walker);
		setFirst(new Recurse<T,E>(this));
	}
	
//	public DepthFirst(Class<T> type, Action<? super T,? extends E> action) {
//		super(type,null, action);
//		setFirst(new Recurse<T,E>(type,this));
//	}

}
