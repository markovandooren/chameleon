package be.kuleuven.cs.distrinet.chameleon.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PluginContainerImpl<P extends Plugin> implements PluginContainer<P> {

  /**
   * Return the connector corresponding to the given connector interface.
   */
 /*@
   @ public behavior
   @
   @ pre connectorInterface != null;
   @*/
	@Override
  public <T extends P> T plugin(Class<T> pluginInterface) {
      return _plugins.get(pluginInterface);
  }

  @Override
	public <T extends P> void removePlugin(Class<T> pluginKind) {
		T old = _plugins.get(pluginKind);
		_plugins.remove(pluginKind);
		if (old!=null && old.container() == this) {
			old.setContainer(null, pluginKind);
		}
	}

	public <K extends P, V extends K> void setPlugin(Class<K> keyInterface, V plugin) {
  K old = _plugins.get(keyInterface);
  if (old!=plugin) {
      if ((plugin!=null) && (plugin.container()!=this)) {
          plugin.setContainer(this, keyInterface);
      }
      // Clean up old backpointer
      if (old!=null) {
          old.setContainer(null, keyInterface);
      }
      // Either
      if(plugin != null) {
      	// Add connector to map
        _plugins.put(keyInterface, plugin);
      } else {
      	// Remove entry in map
      	_plugins.remove(keyInterface);
      }
  }
	}

	
  public Collection<P> plugins() {
    return _plugins.values();
}


  private MapWrapper<P> _plugins = new MapWrapper<P>();
	
  private static class MapWrapper<T> {
    private Map<Class<? extends T>,T> _map = new HashMap<Class<? extends T>,T>();

    public <S extends T> S get(Class<S> key) {
        return (S)_map.get(key);
    }
    
    public Set<Entry<Class<? extends T>,T>> entrySet() {
    	return _map.entrySet();
    }

    public <S extends T> void put(Class<? extends S> key, S value) {
        _map.put(key,value);
    }
    
    public void putAll(MapWrapper<T> other) {
    	_map.putAll(other._map);
    }

    public <S extends T> void remove(Class<S> key) {
        _map.remove(key);
    }

    public Collection<T> values() {
        return _map.values();
    }

    public <S extends T> boolean containsKey(Class<S> key) {
        return _map.containsKey(key);
    }

    public boolean isEmpty() {
        return _map.isEmpty();
    }
}

	@Override
	public <T extends P> boolean hasPlugin(Class<T> keyInterface) {
		return _plugins.containsKey(keyInterface);
	}

	@Override
	public boolean hasPlugins() {
    return ! _plugins.isEmpty();
	}

	@Override
	public Set<Entry<Class<? extends P>, P>> pluginEntrySet() {
		return _plugins.entrySet();
	}

	@Override
	public void clonePluginsFrom(PluginContainer<P> from) {
		for(Entry<Class<? extends P>, P> entry: from.pluginEntrySet()) {
			Class<P> key = (Class<P>) entry.getKey();
			P value = (P) entry.getValue();
			_plugins.put(key, (P)value.clone());
		}
	}

}
