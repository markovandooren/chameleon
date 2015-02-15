package be.kuleuven.cs.distrinet.chameleon.plugin;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;

/**
 * An interface for plugins that offer additional functionality to a tool. 
 * 
 * A tool sometimes needs functionality that cannot be provided in the language
 * module itself because it would introduce tool-specific code. Methods that offer
 * this functionality are placed in a specific Plugin interface.
 * To be able to use a particular language with that tool, that interface must be
 * implemented for that language, and an object of that plugin implementation must be added
 * to the language object of the model using the setPlugin method in Language. 
 * The tool requests the connector object using the getPlugin(PluginInterface.class) 
 * method in Language.
 * 
 * For each plugin interface, there is only one plugin implementation attached to a language.
 * If multiple 'plugins' can be added, you must use the Processor interface.
 * 
 * @author Marko van Dooren
 */
public interface LanguagePlugin extends Plugin<Language, LanguagePlugin> {

	  /**
	   * Return the language to which this connector is connected.
	   */
    public Language language();

    /**
     * Set the language to which this plugin is connected. The bidirectional
     * relation is kept in a consistent state.
     * 
     * T, which represents the plugin interface, must be a super type of the type of this object.
     * 
     * @param lang
     * @param connectorInterface
     */
    public <T extends LanguagePlugin> void setLanguage(Language lang, Class<T> pluginInterface);
    
    /**
     * Clone this connector.
     * @return
     */
    @Override
   public LanguagePlugin clone();
    
}
