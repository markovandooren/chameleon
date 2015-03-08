package org.aikodi.chameleon.plugin;

import java.util.List;
import java.util.Map;

/**
 * A container of processors. A processor is kind of plugin object of which
 * many can be active at the same time. For example, if multiple tools are
 * interested in obtaining the positions of element in documents, they
 * can do their own processing in classes that implement as shared processor
 * interface, and each register their own processor.
 * 
 * A client can request all processors of a given type and invoke
 * methods on them.
 * 
 * @author Marko van Dooren
 *
 * @param <P> The type of the processors in the container.
 */
public interface ProcessorContainer<P extends Processor> {

   /**
    * Return the processors corresponding to the given key interface.
    */
  /*@
    @ public behavior
    @
    @ pre keyInterface != null;
    @
    @ post \result != null;
    @*/
   public <T extends P> List<T> processors(Class<T> keyInterface);

   /**
    * Remove the given processor from the key processor interface. The
    * bidirectional relation is kept in a consistent state.
    * 
    * @param keyType The object representing the type of processor that will
    *                     be used as the key in the map.
    * @param processor The actual processor to be removed from the key.
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
    * @param keyType The object representing the type of processor that will
    *                     be used as the key in the map.
    * @param processor The actual processor to be registered under the key.
    */
  /*@
    @ public behavior
    @
    @ pre keyType != null;
    @ pre processor != null;
    @
    @ post processors(keyType).contains(processor); 
    @*/
	public <K extends P, V extends K> void addProcessor(Class<K> keyType, V processor);
	

	/**
	 * @return The mapping between objects representing types of processors
	 *         and lists containing instances of these types. 
	 */
   public Map<Class<? extends P>, List<? extends P>> processorMap();
  
   /**
    * Copy the processor mapping from the given processor container to this 
    * processor container.
    * 
    * @param The container from which the copy the processors.
    */
  /*@
    @ public behavior
    @
    @ post (\forall Class<? extends Processor> cls; from.processorMap().containsKey(cls);
    @         processors(cls).containsAll(from.processorMap().valueSet());
    @*/
	public void cloneProcessorsFrom(ProcessorContainer<P> from);
}
