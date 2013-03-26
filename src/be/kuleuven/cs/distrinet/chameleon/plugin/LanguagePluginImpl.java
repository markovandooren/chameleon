package be.kuleuven.cs.distrinet.chameleon.plugin;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;

/**
 * @author Marko van Dooren
 */
public abstract class LanguagePluginImpl extends PluginImpl<Language, LanguagePlugin> implements LanguagePlugin {

    public Language language() {
    	return container();
    }

    /**
     * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
     */
    public <T extends LanguagePlugin> void setLanguage(Language lang, Class<T> pluginInterface) {
    	setContainer(lang, pluginInterface);
    }

    public abstract LanguagePluginImpl clone();
}
