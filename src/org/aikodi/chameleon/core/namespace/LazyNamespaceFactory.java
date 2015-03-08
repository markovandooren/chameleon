package org.aikodi.chameleon.core.namespace;


public class LazyNamespaceFactory implements NamespaceFactory {

	@Override
	public Namespace create(String name) {
		return new LazyNamespace(name);
	}

}
