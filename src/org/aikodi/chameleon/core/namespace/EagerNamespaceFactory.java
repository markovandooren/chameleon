package org.aikodi.chameleon.core.namespace;



public class EagerNamespaceFactory implements NamespaceFactory {

	@Override
	public Namespace create(String name) {
		return new EagerNamespace(name);
	}

}
