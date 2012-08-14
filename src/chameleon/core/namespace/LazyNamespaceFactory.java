package chameleon.core.namespace;

import chameleon.plugin.Plugin;
import chameleon.plugin.PluginImpl;

public class LazyNamespaceFactory extends PluginImpl implements NamespaceFactory {

	@Override
	public Namespace create(String name) {
		return new LazyNamespace(name);
	}

	@Override
	public Plugin clone() {
		return new LazyNamespaceFactory();
	}

}
