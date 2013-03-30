package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;

/**
 * An invocation target is a "cross-reference" element to an element in which 
 * declaration can be looked up. Examples are expressions, the "super" keyword
 * in Java.
 * 
 * NOTE: I think that this should become a cross-reference. Investigate!
 * 
 * @author Marko van Dooren
 */

public interface CrossReferenceTarget extends Element {

	/**
	 * Return the target context of this target.
	 *
	 * A target context is the context used to look up elements that are expressed
	 * relative to a target. For example, when looking up <code>a.b</code>, 
	 * first <code>a</code> is looked up in the current context. After that, 
	 * <code>b</code> must be looked up in the context of the element returned by the 
	 * lookup of <code>a</code>. But <code>b</code> must <b>not</b> be lookup up as 
	 * if it were used in the lexical context of the class representing the type of 
	 * <code>a</code>. Therefore, two contexts are provided: a lexical context and 
	 * a target context.
	 *
	 * For example:
	 *   1) in "expr.f", "f" must be looked up in the static type of "expr",
	 *      and not in its lexical context, which is the current lexical context.
	 *   2) in "typename.f", "f" must be looked up in the type represented by "typename"
	 *   3) in "packagename.f", "f" must be looked up in the package represented by "package"
	 */
	public abstract LookupContext targetContext() throws LookupException;


  public abstract CrossReferenceTarget clone();
  
}
