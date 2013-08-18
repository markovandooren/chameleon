package be.kuleuven.cs.distrinet.chameleon.plugin;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;

public interface LanguageProcessor extends Processor<Language, LanguageProcessor>{


	/**
	 * Return the language of this language processor.
	 * @return
	 */
  public Language language();

  /**
   * Set the language to which this processor is connected. The bidirectional
   * relation is kept in a consistent state.
   * 
   * T, which represents the processor interface, must be a super type of the type of this object.
   * 
   * @param view
   * @param connectorInterface
   */
  public <T extends LanguageProcessor> void setLanguage(Language language, Class<T> keyInterface);

}
