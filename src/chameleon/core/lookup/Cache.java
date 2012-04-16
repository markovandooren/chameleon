package chameleon.core.lookup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chameleon.core.declaration.Declaration;

public class Cache {

	public boolean search(Collector<?> collector) throws LookupException {
		boolean result = false;
		DeclarationSelector<?> selector = collector.selector();
		if(selector.canBeCached()) {
			String selectionName = selector.selectionName(null);
			Class selectedClass = selector.selectedClass();
			List cached = get(selectionName, selectedClass);
			if(cached != null) {
				collector.storeCachedResult(cached);
				result = true;
			} 
		}
		return result;
	}
	
	public void store(Collector<?> collector) throws LookupException {
		if(! collector.willProceed()) {
			DeclarationSelector<?> selector = collector.selector();
			if(selector.canBeCached()) {
				String selectionName = selector.selectionName(null);
				Class selectedClass = selector.selectedClass();
				Object result = collector.result();
				if(! selectedClass.isInstance(result)) {
					System.out.println("debug");
				}
				List cache = Collections.singletonList(result);
				put(selectionName, selectedClass,cache);
			}
		}
	}
	
	private synchronized <D extends Declaration> List<D> get(String name, Class<D> kind) {
		if(_nameCache != null) {
			Map<Class,List<? extends Declaration>> classMap = _nameCache.get(name);
			if(classMap != null) {
				return (List<D>) classMap.get(kind);
			}
		}
		return null;
	}
	
	public synchronized <D extends Declaration> void put(String name, Class<D> kind, List<D> declaration) {
		if(_nameCache == null) {
			_nameCache = new HashMap<String,Map<Class,List<? extends Declaration>>>();
		}
		Map<Class,List<? extends Declaration>> classMap = _nameCache.get(name);
		if(classMap == null) {
			classMap = new HashMap<Class,List<? extends Declaration>>();
			_nameCache.put(name, classMap);
		}
		classMap.put(kind, declaration);
	}

	private Map<String,Map<Class,List<? extends Declaration>>> _nameCache;

}
