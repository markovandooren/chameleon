package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * <p>An invocation target is a "cross-reference" element to an element in which 
 * declaration can be looked up. Examples are expressions, the "super" keyword
 * in Java.</p>
 * 
 * @author Marko van Dooren
 */

public interface CrossReferenceTarget extends Element {

	/**
	 * @return <p>Return the target context of this target.</p>
	 *
	 * <p>A target context is the context used to look up elements that are expressed
	 * relative to a target. For example, when looking up <code>a.b</code>, 
	 * first <code>a</code> is looked up in the current context. After that, 
	 * <code>b</code> must be looked up in the context of the element returned by the 
	 * lookup of <code>a</code>. But <code>b</code> must <b>not</b> be lookup up as 
	 * if it were used in the lexical context of the class representing the type of 
	 * <code>a</code>. Therefore, two contexts are provided: a lexical context and 
	 * a target context.</p>
	 *
	 * <p>For example:</p>
	 * <ol>
	 *   <li>in "expr.f", "f" must be looked up in the static type of "expr",
	 *  and not in its lexical context, which is the current lexical context.</li>
	 *   <li>in "typename.f", "f" must be looked up in the type represented by "typename"</li>
	 *   <li>in "packagename.f", "f" must be looked up in the package represented by "package"</li>
	 * </ol>
	 */
	public LocalLookupContext<?> targetContext() throws LookupException;

}
