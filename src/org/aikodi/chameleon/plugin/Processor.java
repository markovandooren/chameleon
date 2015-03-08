package org.aikodi.chameleon.plugin;



/**
 * An interface for classes that connect a language module to a tool, or tools to each other.
 * 
 * Different tools sometimes need to execute code when certain events occur. For example, during
 * the construction of a file, tools may need to store the positions of elements in metadata tags.
 * Because multiple tools may need to process this information, the language object keeps a list of
 * processors for a given process interface.
 * 
 * @author Marko van Dooren
 */
public interface Processor<C extends ProcessorContainer<P>, P extends Processor<C,P>> extends Cloneable {
	
  /**
   * Return the language to which this processor is connected.
   */
  public C container();

  /**
   * Set the language to which this processor is connected. The bidirectional
   * relation is kept in a consistent state.
   * 
   * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
   * 
   * @param lang
   * @param keyInterface
   */
  public <T extends P> void setContainer(C container, Class<T> keyInterface);
  
  /**
   * Clone this processor.
   * @return
   */
  public Processor<C,P> clone();

  
}
