package org.aikodi.chameleon.util.action;

import org.aikodi.chameleon.core.element.Element;

public class TopDown<T,E extends Exception> extends Sequence<T,E> {

	public TopDown(TreeAction<T,? extends E> walker) {
		super(walker, null);
		setSecond(new Recurse<T,E>(this));
	}

}
