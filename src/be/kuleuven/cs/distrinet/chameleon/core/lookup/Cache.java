package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public class Cache {

	/**
	 * Search the cache 
	 * @param collector
	 * @return
	 * @throws LookupException
	 */
	public boolean search(Collector collector) throws LookupException {
		boolean result = false;
		DeclarationSelector<?> selector = collector.selector();
		Declaration cached = selector.readCache(this);
		if(cached != null) {
//			CrossReferenceTest.TRACKER.increase(this);
			collector.storeCachedResult(cached);
			result = true;
		} else {
//			CrossReferenceTest.TRACKER.increase(CacheMiss.MISS);
		}
		return result;
	}
	
//	private static class CacheMiss {
//		public final static CacheMiss MISS = new CacheMiss();
//	}
	
	public void store(Collector collector) throws LookupException {
		if(! collector.willProceed()) {
			DeclarationSelector selector = collector.selector();
			selector.updateCache(this, collector.result());
		}
	}
	
	/**
	 * Store the given object as a cache for the given selector. The
	 * object is stored with key {@code selector.getClass()}.
	 * @param selector
	 * @param object
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void put(DeclarationSelector selector, Object object) {
//		if(_cache == null) {
//			_cache = new HashMap<Class,Object>();
//		}
		_cache.put(selector.getClass(), object);
	}
	
	public synchronized Object get(DeclarationSelector selector) {
//		if(_cache != null) {
			return  _cache.get(selector.getClass());
//		} else {
//			return null;
//		}
	}

	/**
	 * The selectors themselves know the most efficient way to update and retrieve the cache,
	 * so we use Object as the value type.
	 */
//	private Map<Class,Object> _cache;
	private final Map<Class,Object> _cache = new HashMap<Class,Object>();
	
}
