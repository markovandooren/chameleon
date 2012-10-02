package chameleon.workspace;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.rejuse.junit.Revision;

import chameleon.core.language.Language;

public class LanguageRepository {

	public Language get(String name, Revision revision) {
		Map<Revision,Language> tmp = _languageMap.get(name);
		if(tmp != null) {
			Language result = tmp.get(revision);
			if(result != null) {
				return result;
			} else {
				throw new IllegalArgumentException("Revision "+revision.toString()+ " of language "+name+" is not registered.");
			}
		}
		throw new IllegalArgumentException("No language with name "+name+" is registered.");
	}
	
	
	/**
	 * Return the default version of the language with the given name. When an
	 * explicit default version is set, that version is returned. Otherwise, the
	 * highest version (as defined by {@link Revision#compareTo(Revision)}) is returned.
	 * @param name
	 * @return
	 */
	public Language get(String name) {
		Revision rev = _defaultRevisions.get(name);
		if(rev == null) {
			Map<Revision, Language> tmp = _languageMap.get(name);
			// Doesn't want to compile with types in place.
			Set revisions = tmp.keySet();
			rev = Collections.max(revisions);
		}
		// If rev == null, {@link LanguageRepository#get(String,Revision)} will throw an exception.
		return get(name, rev);
	}
	
	public void setDefaultVersion(String languageName, Revision version) {
		if(version == null || languageName == null) {
			throw new IllegalArgumentException();
		}
		_defaultRevisions.put(languageName, version);
	}
	
	
	public void clearDefaultVersion(String languageName) {
		_defaultRevisions.remove(languageName);
	}
	
	private Map<String,Map<Revision,Language>> _languageMap = new HashMap<>();
	
	private Map<String, Revision> _defaultRevisions = new HashMap<>();
}
