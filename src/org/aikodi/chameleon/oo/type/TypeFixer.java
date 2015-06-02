package org.aikodi.chameleon.oo.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.element.LookupExceptionInEquals;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;

/**
 * A class for computing the fixed point of the subtype relation when
 * type variables are used. 
 * 
 * @author Marko van Dooren
 */
public class TypeFixer extends Fixer<Type,Object> {

//	private Map<Type,Object> _map;
//	
//	/**
//	 * Indicate that the match between the given type and
//	 * the given type parameter has been checked. The
//	 * next time, the check can be skipped.
//	 * 
//	 * @param type The type that has been checked with the given type parameter. 
//	 * @param parameter The type parameter that has been checked with the given type.
//	 */
//	public void add(Type type, TypeParameter parameter) {
//		if(_map == null) {
//			_map = new HashMap<>();
//		}
//		_map.merge(type, parameter, (oldValue, newValue) -> {
//			if(oldValue == newValue) {
//				return oldValue;
//			} else if(oldValue instanceof Set) {
//				((Set)oldValue).add(newValue);
//				return oldValue;
//			} else if(! oldValue.equals(newValue)){
//				Set set = new HashSet();
//				set.add(oldValue);
//				set.add(newValue);
//				return set;
//			} else {
//				return oldValue;
//			}
//		});
//	}
//	
//	/**
//	 * Check whether the given type has already been checked with the given type parameter.
//	 * 
//	 * @param type The type for which must be checked whether it has already been checked
//	 * with the given type parameter.
//	 * @param parameter The type parameter for which must be checked whether it has already been checked
//	 * with the given type.
//	 * @return True if the given type has already been checked with the given type parameter.
//	 * False otherwise.
//	 * @throws LookupException
//	 */
//	public boolean contains(Type type, TypeParameter parameter) throws LookupException {
//		try {
//			boolean result = false;
//			if(_map != null) {
//				Object object = _map.get(type);
//				if(object instanceof Set) {
//					result = ((Set)object).contains(parameter);
//				} else {
//					result = parameter.equals(object);
//				}
//			}
//			return result;
//		} catch(LookupExceptionInEquals e) {
//			throw (LookupException)e.getCause();
//		}
//	}
//	
//	/**
//	 * Clone this type fixer. The resulting type fixer has the same mapping
//	 * as this type fixer.
//	 */
//	public TypeFixer clone() {
//		TypeFixer result = new TypeFixer();
//		if(_map != null) {
//			_map.forEach((k,v) -> {
//				if(v instanceof Set) {
//					((Set)v).forEach(t -> result.add(k,(TypeParameter)t));
//				} else {
//					result.add(k, (TypeParameter) v);
//				}
//			});
//		}
//		return result;
//	}
	
	@Override
	public TypeFixer clone() {
		TypeFixer result = new TypeFixer();
		if(_map != null) {
			_map.forEach((k,v) -> {
				if(v instanceof Set) {
					((Set)v).forEach(t -> result.add(k, t));
				} else {
					result.add(k, v);
				}
			});
		}
		return result;

	}
	
}
