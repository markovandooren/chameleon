package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A class for computing the fixed point of the subtype relation when
 * type variables are used. 
 * 
 * @author Marko van Dooren
 */
public class Fixer<F,S> {

	protected Map<F,Object> _map;
	
	/**
	 * Indicate that the match between the given type and
	 * the given type parameter has been checked. The
	 * next time, the check can be skipped.
	 * 
	 * @param first The type that has been checked with the given type parameter. 
	 * @param second The type parameter that has been checked with the given type.
	 */
	public void add(F first, S second) {
		if(_map == null) {
			_map = new IdentityHashMap<>();
		}
		_map.merge(first, second, (oldValue, newValue) -> {
			if(oldValue == newValue) {
				return oldValue;
			} else if(oldValue instanceof Map) {
				((Map)oldValue).put(newValue,newValue);
				return oldValue;
			} else //if(! oldValue.equals(newValue))
			{
				Map set = new IdentityHashMap<>();
				set.put(oldValue,oldValue);
				set.put(newValue,newValue);
				return set;
			}
//			else {
//				return oldValue;
//			}
		});
	}
	
	/**
	 * Check whether the given type has already been checked with the given type parameter.
	 * 
	 * @param first The type for which must be checked whether it has already been checked
	 * with the given type parameter.
	 * @param second The type parameter for which must be checked whether it has already been checked
	 * with the given type.
	 * @return True if the given type has already been checked with the given type parameter.
	 * False otherwise.
	 * @throws LookupException
	 */
	public boolean contains(F first, S second) throws LookupException {
//		try {
			boolean result = false;
			if(_map != null) {
				Object object = _map.get(first);
				if(object instanceof Map) {
					result = ((Map)object).containsKey(second);
				} else {
					result = (second == object);
				}
			}
			return result;
//		} catch(LookupExceptionInEquals e) {
//			throw (LookupException)e.getCause();
//		}
	}
	
	/**
	 * Clone this type fixer. The resulting type fixer has the same mapping
	 * as this type fixer.
	 */
	public Fixer<F,S> clone() {
		Fixer result = new Fixer<F,S>();
		if(_map != null) {
			_map.forEach((k,v) -> {
				if(v instanceof Map) {
					((Map<F,S>)v).entrySet().forEach(p -> result.add(k,(S)p.getValue()));
				} else {
					result.add(k, (S) v);
				}
			});
		}
		return result;
	}
}
