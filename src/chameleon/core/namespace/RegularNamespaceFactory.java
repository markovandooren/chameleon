package chameleon.core.namespace;

import chameleon.plugin.Plugin;
import chameleon.plugin.PluginImpl;

public class RegularNamespaceFactory extends PluginImpl implements NamespaceFactory {

	@Override
	public Namespace create(String name) {
		return new RegularNamespace(name);
	}

	@Override
	public Plugin clone() {
		return new RegularNamespaceFactory();
	}

}
