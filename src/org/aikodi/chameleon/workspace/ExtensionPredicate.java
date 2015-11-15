package org.aikodi.chameleon.workspace;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.util.Strings;
import org.aikodi.chameleon.util.Util;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

/**
 * A class of predicate that match a file based on its extension.
 * 
 * @author Marko van Dooren
 */
public class ExtensionPredicate extends SafePredicate<String> {

	/**
	 * Create a new extension predicate with the given extensions.
	 * @param extensions
	 */
 /*@
   @ public behavior
   @
   @ pre extensions.length > 0;
   @ pre ! extensions.contains(null);
   @
   @ post extensions().size() == extensions.length;
   @ post extensions().containsAll(extensions);
   @*/
	public ExtensionPredicate(String ... extensions) {
		if(extensions.length == 0) {
			throw new IllegalArgumentException("An extension predicate cannot be initialized with an empty list of extensions");
		}
		for(String extension: extensions) {
			if(extension == null) {
				throw new IllegalArgumentException("A null reference is not a valid extension");
			}
			_extensions.add(extension);
		}
	}
	
	/**
	 * Return the extensions of files for which the {@link #eval(String)} method
	 * of this extension predicate will return true.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post ! \result.isEmpty();
   @*/
	public Collection<String> extensions() {
		return Collections.unmodifiableCollection(_extensions);
	}
	
	private Set<String> _extensions = new HashSet<String>();
	
	/**
	 * Returns true if the given file name has an extension that is
	 * a member of the set of extensions of this extension predicate. 
	 */
 /*@
   @ public behavior
   @
   @ \result == extensions().contains(Util.getLastPart(fileName));
   @*/
	@Override
	public boolean eval(String fileName) {
		boolean result = false;
		if(fileName.contains(".")) {
			String extension = Util.getLastPart(fileName);
			result = _extensions.contains(extension);
		}
		return result;
	}

	@Override
	public String toString() {
		return "Extension predicate: "+Strings.create(extensions());
	}
	
}
