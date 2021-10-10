package org.aikodi.chameleon.oo.method.exception;

import org.aikodi.chameleon.oo.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marko van Dooren
 */
public class TypeMap {
  
  private Map _map = new HashMap();
  
  public void add(String name, Type type) {
    _map.put(name, type);
  }
  
  public Type getType(String name) {
    return (Type)_map.get(name);
  }
  
  public boolean contains(String name) {
    return _map.containsKey(name);
  }
}
