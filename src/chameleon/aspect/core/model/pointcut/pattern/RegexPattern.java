package chameleon.aspect.core.model.pointcut.pattern;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;

/**
 * A pattern that uses a regular expression for matching.
 * 
 * @author Marko van Dooren
 */
public class RegexPattern extends DeclarationPattern {

	/**
	 * Initialize a new regular expression pattern with the given regular expression.
	 * @param regex The regular expression
	 */
 /*@
   @ public behavior
   @
   @ pre regex != null;
   @
   @ post regex() == regex;
	 @*/
	public RegexPattern(String regex) {
		_regex = regex;
	}

 /*@
   @ public behavior 
   @
   @ post \result == declaration.signature().name().matches(pattern().replace("*", ".*"));
   @*/
	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		Signature signature = declaration.signature();
		return signature != null && signature.name().matches(regex());
	}
	

	/**
	 * Return the regular expression
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String regex() {
		return _regex;
	}
	
	private String _regex;

	@Override
	public DeclarationPattern clone() {
		return new RegexPattern(regex());
	}
}
