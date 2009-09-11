package chameleon.test.provider;

import chameleon.core.element.Element;

public abstract class AbstractDescendantProvider<E extends Element> implements ElementProvider<E> {

	
	public AbstractDescendantProvider(ElementProvider<? extends Element> ancestorProvider, Class<E> cls) {
		super();
		_ancestorProvider = ancestorProvider;
		_cls = cls;
	}

	private ElementProvider<? extends Element> _ancestorProvider;
	private Class<E> _cls;

	/**
	 * Return the provider that provides the ancestors in which the descendants
	 * are searched.
	 */
	public ElementProvider<? extends Element> ancestorProvider() {
		return _ancestorProvider;
	}

	/**
	 * Return the class object for the type of the elements to be searched.
	 */
	public Class<E> elementType() {
		return _cls;
	}
	
}
