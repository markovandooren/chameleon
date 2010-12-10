package chameleon.plugin;

import chameleon.core.language.Language;

/**
 * @author Marko van Dooren
 */
public abstract class PluginImpl implements Plugin {

    private Language _language;

    public Language language() {
        return _language;
    }

    /**
     * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
     */
    public <T extends Plugin> void setLanguage(Language lang, Class<T> pluginInterface) {
    	if (lang!=_language) {
    		Language old = _language;
    		// 1) set _language
    		_language = lang;
    		// 2) remove old backpointer
    		if (old!=null) {
    			old.removePlugin(pluginInterface);
    		}
    		// 3) set new backpointer
    		if (_language!=null) {
    			_language.setPlugin(pluginInterface, (T)this);
    		}
    	}
    }

    public abstract Plugin clone();
}
