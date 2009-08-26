package chameleon.tool;

import chameleon.core.language.Language;

/**
 * An interface for classes that connect a language module to a tool.
 * 
 * A tool sometimes needs functionality that cannot be provided in the language
 * module itself because it would introduce tool-specific code. Method that offer
 * this functionality are placed in a specific Connector interface for that tool.
 * To be able to use a particular language with that tool, that interface must be
 * implemented for that tool, and an object of that connector class must be added
 * to the language object of the model using the setConnector method in Language. 
 * The tool requests the connector object using the getConnector(ConnectorInterface.class) 
 * method in Language.
 * 
 * For each connector interface, there is only one connector attached to a language object.
 * If multiple 'connectors' can be added, you must use the Processor interface.
 * 
 * @author Marko van Dooren
 */
public interface Connector extends Cloneable {

	  /**
	   * Return the language to which this connector is connected.
	   */
    public Language language();

    /**
     * Set the language to which this connector is connected. The bidirectional
     * relation is kept in a consistent state.
     * 
     * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
     * 
     * @param lang
     * @param connectorInterface
     */
    public <T extends Connector> void setLanguage(Language lang, Class<T> connectorInterface);
    
    /**
     * Clone this connector.
     * @return
     */
    public Connector clone();
    
}
