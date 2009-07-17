package chameleon.core.declaration;

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
public interface Definition<E extends Definition<E,P,S,F>, P extends DeclarationContainer,S extends Signature, F extends Declaration> extends Declaration<E,P,S,F> {

	// SIMPLIFY: Can't we move complete() to Declaration?
	
	/**
	 * Check if this definition is complete.
	 * @return
	 */
	public boolean complete();
}
