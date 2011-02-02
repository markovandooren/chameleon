package chameleon.oo.type.inheritance;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;

public abstract class RenamingClause<E extends RenamingClause<E,P>,P extends Element> extends ElementImpl<E> {

	/**
	 * Check if this renaming clause can deal with the given signature.
	 * 
	 * @param signature
	 * @return
	 */
	public abstract boolean appliesTo(Signature signature);
	
	/**
	 * Apply this renaming to the given signature.
	 * 
	 * @param signature
	 * @return
	 */
	public abstract Signature rename(Signature signature);
}
