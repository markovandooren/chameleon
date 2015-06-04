/**
 * 
 */
package org.aikodi.chameleon.oo.type;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.findFirst;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aikodi.chameleon.core.lookup.LookupException;

import com.google.common.collect.ImmutableList;

public class SuperTypeJudge {
  // Nasty internal structure to prevent the creation
  // of a lot of lists with just a single element.
  private Map<String,Object> _map = new HashMap<>();

  void add(Type type) throws LookupException {
    Object o = _map.get(type.name());
    if(o == null) {
      _map.put(type.name(), type);
    } else if(o instanceof List) {
      _map.put(type.name(),ImmutableList.builder().add(type).addAll((List)o).build());
    } else {
      //Check for duplicates
      if(! ((Type)o).baseType().sameAs(type.baseType())) {
        _map.put(type.name(), ImmutableList.builder().add(type).add(o).build());
      }
    }
  }

  Set<Type> types() {
    Set<Type> result = new HashSet<>();
    for(Object o: _map.values()) {
      if(o instanceof List) {
        result.addAll((List)o);
      } else {
        result.add((Type) o);
      }
    }
    return result;
  }

  public Type get(Type baseType) throws LookupException {
    final Type realBase = baseType.baseType();
    Object o = _map.get(realBase.name());
    if(o instanceof List) {
      return findFirst((List<Type>) o, t -> t.baseType().sameAs(realBase));
    } else {
      Type stored = (Type)o;
      return stored == null ? null : (stored.baseType().sameAs(realBase) ? stored : null);
    }
  }

  void merge(SuperTypeJudge superJudge) throws LookupException {
    for(Object v :superJudge._map.values()) {
      if(v instanceof List) {
        for(Type t: (List<Type>)v) {
          add(t);
        }
      } else {
        add((Type)v);
      }
    }
  }
}