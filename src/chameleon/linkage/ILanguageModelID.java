package chameleon.linkage;

import chameleon.output.Syntax;

/**
 * @author Joeri Hendrickx
 * 
 * A Language Identification Interface to be implemented by all Language Models
 * The implementationclass must be called "LanguageModelID" and must be sat on the root
 * of the language-model-package
 */
public interface ILanguageModelID {

	/**
	 * @return Informal name of the supported language
	 */
	String getLanguageName();
	
	/**
	 * @return Version information of the supported language
	 */
	String getLanguageVersion();
	
	/**
	 * @return Version information of the Language Model
	 */
	String getVersion();
	
	/**
	 * @return General description of this language model
	 */
	String getDescription();
	
	/**
	 * @return Licensing information for the language model
	 */
	String getLicense();
	
	/**
	 * @return the MetaModelFactory for this language
	 */
	IMetaModelFactory getMetaModelFactory();
	
	/**
	 * @return the CodeWriter for this language
	 */
	Syntax getCodeWriter();
	
	
}
