package org.aikodi.chameleon.aspect.core.weave.registry;

import java.util.HashMap;
import java.util.Map;

import org.aikodi.chameleon.core.element.Element;

public class NamingRegistryFactory {
	
	private NamingRegistryFactory() {
		
	}
	
	private static NamingRegistryFactory instance = new NamingRegistryFactory();
	
	//TODO Remove this singleton. This is a disaster waiting to happen when dealing with multiple projects.
	public static NamingRegistryFactory instance() {
		return instance;
	}
	
	Map<String, NamingRegistry> registries = new HashMap<String, NamingRegistry>();
	
	public <T extends Element> NamingRegistry<T> getNamingRegistryFor(String name) {
		if (!registries.containsKey(name))
			registries.put(name, new NamingRegistry<T>());
		
		return registries.get(name);
	}
}
