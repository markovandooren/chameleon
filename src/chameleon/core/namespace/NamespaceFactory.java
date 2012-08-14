package chameleon.core.namespace;

import chameleon.plugin.Plugin;

public interface NamespaceFactory extends Plugin {

	public Namespace create(String name);
}
