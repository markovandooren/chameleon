package org.aikodi.chameleon.core.language;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.util.Lists;

import com.google.common.collect.ImmutableList;

/**************
  * CONNECTORS *
  **************/


  public class ListMapWrapper<T> {
    private Map<Class<? extends T>,List<? extends T>> _map = new HashMap<Class<? extends T>,List<? extends T>>();

    public int size() {
    	return _map.size();
    }
    
    public Set<Class<? extends T>> keySet() {
    	return _map.keySet();
    }
    
    public Map<Class<? extends T>,List<? extends T>> map() {
    	return new HashMap<Class<? extends T>,List<? extends T>>(_map);
    }
    
    public <S extends T> List<S> get(Class<S> key) {
    	List<S> processors = (List<S>)_map.get(key);
    	if(processors == null) {
    		return ImmutableList.of();
    	} else {
        return Lists.create(processors);
    	}
    }
    
    public void addAll(Map<Class<? extends T>,List<? extends T>> map) {
    	_map.putAll(map);
    }

    public <S extends T> void add(Class<S> key, S value) {
    	  List<S> list = (List<S>)_map.get(key);
    	  if(list == null) {
    	  	list = Lists.create();
    	  	_map.put(key, list);
    	  }
    	  list.add(value);
    }

    public <S extends T> void remove(Class<S> key, S value) {
    	_map.get(key).remove(key);
    }

    public Collection<List<? extends T>> values() {
        return _map.values();
    }

    public <S extends T> boolean containsKey(Class<S> key) {
        return _map.containsKey(key);
    }

    public boolean isEmpty() {
        return _map.isEmpty();
    }
}
