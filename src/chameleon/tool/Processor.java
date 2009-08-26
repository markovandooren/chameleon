package chameleon.tool;

import chameleon.core.language.Language;

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
public interface Processor extends Cloneable {
	
  /**
   * Return the language to which this processor is connected.
   */
  public Language language();

  /**
   * Set the language to which this processor is connected. The bidirectional
   * relation is kept in a consistent state.
   * 
   * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
   * 
   * @param lang
   * @param connectorInterface
   */
  public <T extends Processor> void setLanguage(Language lang, Class<T> connectorInterface);
  
  /**
   * Clone this processor.
   * @return
   */
  public Processor clone();

  
}
