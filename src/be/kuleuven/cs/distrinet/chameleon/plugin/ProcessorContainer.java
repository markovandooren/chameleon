package be.kuleuven.cs.distrinet.chameleon.plugin;

import java.util.List;
import java.util.Map;

public interface ProcessorContainer<P extends Processor> {

  /**
   * Return the plugin corresponding to the given key interface.
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @*/
  public <T extends P> List<T> processors(Class<T> keyInterface);

  /**
   * Remove the given processor from the key plugin interface. The
   * bidirectional relation is kept in a consistent state.
   * 
   * @param <T>
   * @param keyInterface
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @ pre processor != null;
   @
   @ post ! processors(keyInterface).contains(processor);
   @*/
	public <T extends P> void removeProcessor(Class<T> keyInterface, T processor);

  /**
   * Add the processor corresponding to the list associated with the given key plugin interface. 
   * The bidirectional relation is kept in a consistent state.
   * 
   * @param <T>
   * @param keyInterface
   * @param plugin
   */
 /*@
   @ public behavior
   @
   @ pre keyInterface != null;
   @ pre processor != null;
   @
   @ post processors(keyInterface).contains(processor); 
   @*/
	public <K extends P, V extends K> void addProcessor(Class<K> keyInterface, V processor);
	
//  /**
//   * Return all plugins attached to this plugin container.
//   * @return
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @ post (\forall Plugin c; ; \result.contains(c) == 
//   @           (\exists Class<? extends Plugin> keyInterface;; plugin(keyInterface) == c)
//   @      ); 
//   @*/
//  public Collection<P> processors();

	
  

//  /**
//   * Check if this plugin container has a plugin for the given plugin type. Typically
//   * the type is an interface or abstract class for a specific tool.
//   */
// /*@
//   @ public behavior
//   @
//   @ pre keyInterface != null;
//   @
//   @ post \result == plugin(keyInterface) != null;
//   @*/
//  public <T extends P> boolean hasPlugin(Class<T> keyInterface);

//  /**
//   * Check if this plugin container has any plugins.
//   */
// /*@
//   @ public behavior
//   @
//   @ post \result ==  
//   @*/
//  public boolean hasPlugins();

//  public Set<Entry<Class<? extends P>,P>> pluginEntrySet();
  public Map<Class<? extends P>, List<? extends P>> processorMap();
  
	public void cloneProcessorsFrom(ProcessorContainer<P> from);


}
