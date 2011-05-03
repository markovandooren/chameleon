package chameleon.core.declaration;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * A definition is a declaration that needs a definition. Examples are methods and types.
 * A variable is not a definition because it is always complete.
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 * @param <S>
 * @param <F>
 */
public interface Definition<E extends Definition<E,S>, S extends Signature> extends Declaration<E,S> {

	// SIMPLIFY: Can't we move complete() to Declaration?
	
	/**
	 * Check if this definition is complete.
	 * @return
	 * @throws LookupException 
	 */
	public Ternary complete();
}
