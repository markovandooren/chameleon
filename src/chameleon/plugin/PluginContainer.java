package chameleon.plugin;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import chameleon.core.language.Language;

public interface PluginContainer<P extends Plugin> {

  /**
   * Return the plugin corresponding to the given key interface.
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @*/
  public <T extends P> T plugin(Class<T> keyInterface);

  /**
   * Remove the plugin corresponding to the given key plugin interface. The
   * bidirectional relation is kept in a consistent state.
   * 
   * @param <T>
   * @param keyInterface
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @
   @ post plugin(keyInterface) == null;
   @*/
	public <T extends P> void removePlugin(Class<T> keyInterface);

  /**
   * Set the plugin corresponding to the given key plugin interface. The bidirectional relation is 
   * kept in a consistent state.
   * 
   * @param <T>
   * @param keyInterface
   * @param plugin
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @ pre plugin != null;
   @
   @ post plugin(keyInterface) == plugin; 
   @*/
	public <K extends P, V extends K> void setPlugin(Class<K> keyInterface, V plugin);
	
  /**
   * Return all plugins attached to this plugin container.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post (\forall Plugin c; ; \result.contains(c) == 
   @           (\exists Class<? extends Plugin> keyInterface;; plugin(keyInterface) == c)
   @      ); 
   @*/
  public Collection<P> plugins();

	
  

  /**
   * Check if this plugin container has a plugin for the given plugin type. Typically
   * the type is an interface or abstract class for a specific tool.
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @
   @ post \result == plugin(keyInterface) != null;
   @*/
  public <T extends P> boolean hasPlugin(Class<T> keyInterface);

  /**
   * Check if this plugin container has any plugins.
   */
 /*@
   @ public behavior
   @
   @ post \result ==  
   @*/
  public boolean hasPlugins();

  public Set<Entry<Class<? extends P>,P>> pluginEntrySet();
  
	public void clonePluginsFrom(PluginContainer<P> from);


}
