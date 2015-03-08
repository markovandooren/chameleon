package org.aikodi.chameleon.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class for tracking the memory allocation behavior of objects.
 * 
 * For example, if you suspect that an object allocates too many temporary
 * collections, you can let it {@link #increase(Object)} its counter. After
 * running your code, you can then ask an overview per creator or per class
 * of creators to find out where most of the unnecessary allocations are done.
 * Caching everything requires too much error-prone code. This class can help you
 * to track the hot spots.
 * 
 * This class is typically used in code that is thrown away afterwards. You don't want
 * profiling code slowing everything down after you're done profiling. Therefore I
 * usually create a public final static field somewhere and add code to increase the
 * counters. After the run, the field is used for the output. When the most important
 * hot spots are removed, all the profiling code is removed.
 * 
 * @author Marko van Dooren
 */
public class AllocationTracker {

	/**
	 * Increase the allocation count for the given creator.
	 * @param creator
	 */
	public void increase(Object creator) {
		Integer i = _nbAllocations.get(creator);
		if(i == null) {
			_nbAllocations.put(creator, 1);
		} else {
			_nbAllocations.put(creator, 1+i);
		}
	}

	/**
	 * Return the number of avoidable allocations for the given creator.
	 * This number is equal to the total number of allocations for
	 * that creator minus 1. This assumes of course that the first
	 * allocation can be cached.
	 * 
	 * @param creator
	 * @return
	 */
	public int nbAvoidableAllocations(Object creator) {
		int count = 0;
		for(Map.Entry<Object, Integer> entry: _nbAllocations.entrySet()) {
			int intValue = entry.getValue().intValue();
			if(intValue > 1) {
				count += intValue - 1;
			}
		}
		return count;
	}

	public void clearAllocationMap() {
		_nbAllocations = new HashMap<Object, Integer>();
	}

	public Map<Class,Integer> nbAvoidableAllocationsPerClass() {
		Map<Class,Integer> result = new HashMap<Class, Integer>();
		for(Map.Entry<Object, Integer> entry: _nbAllocations.entrySet()) {
			Class c = entry.getKey().getClass();
			Integer count = entry.getValue();
			Integer accumulated = result.get(c);
			if(accumulated == null) {
				accumulated = count - 1;
			} else {
				accumulated = accumulated + count - 1;
			}
			result.put(c, accumulated);
		}
		return result;
	}

	
	public Map<Object, Integer> _nbAllocations = new HashMap<Object, Integer>();
}
