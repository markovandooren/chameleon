package chameleon.aspect.core.model.pointcut.pattern;


/**
 * A wildcard name pattern is a pattern that can make use of wildcards.
 * For example set* will match any declaration whose name starts with "set".
 * 
 * @invariant regex().equals(pattern().replace("*", ".*"));
 * 
 * @author Marko van Dooren
 */
public class WildCardNamePattern extends RegexPattern {

	// 
	
	/**
	 * Initialize a new wildcard name pattern with the given pattern.
	 * @param pattern
	 */
 /*@
   @ public behavior
   @
   @ pre pattern != null;
   @
   @ post pattern() == pattern;
   @ post regex().equals(pattern.replace("*", ".*"));
	 @*/
  public WildCardNamePattern(String pattern) {
  	super(pattern.replace("*", ".*"));
		_pattern = pattern;
  }

	/**
	 * Return the wildcard pattern
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String pattern() {
		return _pattern;
	}

	private String _pattern;
	
	@Override
	public DeclarationPattern clone() {
		return new WildCardNamePattern(pattern());
	}


}
