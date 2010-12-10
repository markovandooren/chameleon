package chameleon.plugin;

import chameleon.core.language.Language;

/**
 * Convenience super class for processors.
 * 
 * @author Marko van Dooren
 */
public abstract class ProcessorImpl implements Processor {

  private Language _language;

  public Language language() {
      return _language;
  }

  /**
   * T MUST BE A SUPERTYPE OF THIS OBJECT!!!
   */
  public <T extends Processor> void setLanguage(Language lang, Class<T> connectorInterface) {
    if (lang!=_language) {
    	  // 1) remove old backpointer
        if (_language!=null) {
            _language.removeProcessor(connectorInterface, (T)this);
        }
        // 2) set _language
        _language = lang;
        // 3) set new backpointer
        if (_language!=null && ! language().processors(connectorInterface).contains(this)) {
            _language.addProcessor(connectorInterface, (T)this);
        }
    }
  }

  public abstract Processor clone();
}
