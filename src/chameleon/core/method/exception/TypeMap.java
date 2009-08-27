package chameleon.core.method.exception;

import java.util.HashMap;
import java.util.Map;

import chameleon.core.type.Type;

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
