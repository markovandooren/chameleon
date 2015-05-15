package org.aikodi.chameleon.oo.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.element.LookupExceptionInEquals;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;

public class TypeFixer {

	private Map<Type,Object> _map;
	
	public void add(Type type, TypeParameter parameter) {
		if(_map == null) {
			_map = new HashMap<>();
		}
		_map.merge(type, parameter, (oldValue, newValue) -> {
			if(oldValue == newValue) {
				return oldValue;
			} else if(oldValue instanceof Set) {
				((Set)oldValue).add(newValue);
				return oldValue;
			} else if(! oldValue.equals(newValue)){
				Set set = new HashSet();
				set.add(oldValue);
				set.add(newValue);
				return set;
			} else {
				return oldValue;
			}
		});
	}
	
	public boolean contains(Type type, TypeParameter parameter) throws LookupException {
		try {
			boolean result = false;
			if(_map != null) {
				Object object = _map.get(type);
				if(object instanceof Set) {
					result = ((Set)object).contains(parameter);
				} else {
					result = parameter.equals(object);
				}
			}
			return result;
		} catch(LookupExceptionInEquals e) {
			throw (LookupException)e.getCause();
		}
	}
	
	public TypeFixer clone() {
		TypeFixer result = new TypeFixer();
		if(_map != null) {
			_map.forEach((k,v) -> {
				if(v instanceof Set) {
					((Set)v).forEach(t -> result.add(k,(TypeParameter)t));
				} else {
					result.add(k, (TypeParameter) v);
				}
			});
		}
		return result;
	}
}
