package be.kuleuven.cs.distrinet.chameleon.plugin;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;

public abstract class LanguageProcessorImpl extends ProcessorImpl<Language, LanguageProcessor> implements LanguageProcessor {

	@Override
   public Language language() {
		return container();
	}
	
	@Override
	public <T extends LanguageProcessor> void setLanguage(Language language, Class<T> keyInterface) {
		setContainer(language, keyInterface);
	}

	@Override
	public abstract LanguageProcessorImpl clone();


}
