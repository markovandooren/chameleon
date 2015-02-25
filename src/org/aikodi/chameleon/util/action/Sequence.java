package org.aikodi.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

public class Sequence<T,E extends Exception> extends TreeAction<T,E> {
	
	public Sequence(TreeAction<T, ? extends E> first, TreeAction<? super T, ? extends E> second) {
		super(first.type());
		_first = first;
		_second = second;
	}

	public Sequence(Class<T> type, TreeAction<T, ? extends E> first, TreeAction<? super T, ? extends E> second) {
		super(type);
		_first = first;
		_second = second;
	}

	private TreeAction<? super T, ? extends E> _first;
	
	public TreeAction<? super T, ? extends E> first() {
		return _first;
	}

	protected void setFirst(TreeAction<? super T, ? extends E> first) {
		_first = first;
	}

	public TreeAction<? super T, ? extends E> second() {
		return _second;
	}

	protected void setSecond(TreeAction<? super T, ? extends E> second) {
		_second = second;
	}

	private TreeAction<? super T, ? extends E> _second;
	
	@Override
	protected void doPerform(T element) throws E {
		first().enter(element);
		first().perform(element);
		second().enter(element);
		second().perform(element);
		second().exit(element);
		first().exit(element);
	}
	@Override
	public void traverse(T element, TreeStructure<? extends T> tree) throws E {          
		first().enter(element);
		first().traverse(element,tree);
		second().enter(element);
		second().traverse(element,tree);
		second().exit(element);
		first().exit(element);
	}
}
