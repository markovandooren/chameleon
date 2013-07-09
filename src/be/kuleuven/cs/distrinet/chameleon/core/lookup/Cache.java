package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

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
//		if(selector.isGreedy()) {
//			String selectionName = selector.selectionName(null);
//			Class selectedClass = selector.selectedClass();
//			List cached = get(selectionName, selectedClass);
//			if(cached != null) {
//				collector.storeCachedResult(cached);
//				result = true;
//			} 
//		}
		return result;
	}
	
	public void store(Collector<?> collector) throws LookupException {
		if(! collector.willProceed()) {
			DeclarationSelector selector = collector.selector();
			selector.updateCache(this, (Declaration) collector.result());
//			if(selector.isGreedy()) {
//				String selectionName = selector.selectionName(null);
//				Class selectedClass = selector.selectedClass();
//				Object result = collector.result();
//				List cache = Collections.singletonList(result);
//				put(selectionName, selectedClass,cache);
//			}
		}
	}
	
//	private synchronized <D extends Declaration> List<D> get(String name, Class<D> kind) {
//		if(_nameCache != null) {
//			Map<Class,List<? extends Declaration>> classMap = _nameCache.get(name);
//			if(classMap != null) {
//				return (List<D>) classMap.get(kind);
//			}
//		}
//		return null;
//	}
//	
//	private synchronized <D extends Declaration> void put(String name, Class<D> kind, List<D> declaration) {
//		if(_nameCache == null) {
//			_nameCache = new HashMap<String,Map<Class,List<? extends Declaration>>>();
//		}
//		Map<Class,List<? extends Declaration>> classMap = _nameCache.get(name);
//		if(classMap == null) {
//			classMap = new HashMap<Class,List<? extends Declaration>>();
//			_nameCache.put(name, classMap);
//		}
//		classMap.put(kind, declaration);
//	}

//	private Map<String,Map<Class,List<? extends Declaration>>> _nameCache;

	public synchronized void put(DeclarationSelector selector, Object object) {
		if(_cache == null) {
			_cache = new HashMap<Class,Object>();
		}
		_cache.put(selector.getClass(), object);
	}
	
	public synchronized Object get(DeclarationSelector selector) {
		if(_cache != null) {
			return  _cache.get(selector.getClass());
		} else {
			return null;
		}
	}

	/**
	 * The selectors themselves know the most efficient way to update and retrieve the cache,
	 * so we use Object as the value type.
	 */
	private Map<Class,Object> _cache;
	
}
