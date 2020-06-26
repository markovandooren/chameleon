package org.aikodi.chameleon.core.lookup;

import java.util.HashMap;
import java.util.Map;

import org.aikodi.chameleon.core.declaration.Declaration;

public class Cache {

	/**
	 * Search the cache 
	 * @param collector
	 * @return
	 * @throws LookupException
	 */
	public boolean search(Collector<?> collector) throws LookupException {
		boolean result = false;
		DeclarationSelector<?> selector = collector.selector();
		Declaration cached = selector.readCache(this);
		if(cached != null) {
			collector.storeCachedResult(cached);
			result = true;
		}
		return result;
	}
	
	public <D extends Declaration> void store(Collector<D> collector) throws LookupException {
		if(! collector.willProceed()) {
			DeclarationSelector<D> selector = collector.selector();
			selector.updateCache(this, collector.result());
		}
	}
	
	/**
	 * Store the given object as a cache for the given selector. The
	 * object is stored with key {@code selector.getClass()}.
	 * 
	 * @param selector The selector for whose <b>type</b> the cache is stored.
	 * @param object 
	 */
	public synchronized void put(DeclarationSelector<?> selector, Object object) {
		_cache.put(selector.getClass(), object);
	}
	
	public synchronized Object get(DeclarationSelector<?> selector) {
		return _cache.get(selector.getClass());
	}

	/**
	 * The selectors themselves know the most efficient way to update and retrieve the cache,
	 * so we use Object as the value type.
	 */
	private final Map<Class<?>,Object> _cache = new HashMap<Class<?>,Object>();
	
}
