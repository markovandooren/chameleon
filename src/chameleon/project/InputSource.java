package chameleon.project;

import java.io.IOException;

import chameleon.core.namespace.RootNamespace;
import chameleon.input.ParseException;

/**
 * A class representing sources from which a mode is built. These
 * can be file based, editor based,...
 * 
 * @author Marko van Dooren
 * @author Nelis Boucke
 */
public abstract class InputSource {
	
	/**
	 * Create a new input source that puts its elements in the given
	 * default namespace.
	 */
 /*@
   @ public behavior
   @
   @ pre defaultNamespace != null;
   @*/
	public InputSource(RootNamespace defaultNamespace) {
		_defaultNamespace = defaultNamespace;
	}

	private RootNamespace _defaultNamespace;
	
	/**
	 * Return the root namespace in which the elements from this input
	 * source are put.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public RootNamespace defaultNamespace() {
		return _defaultNamespace;
	}
	
	/**
	 * Perform a clean read of the input. This method is called when the model
	 * is built initially or when the underlying input source is modified. It is
	 * the responsibility of the implementation to ensure that a refresh is idempotent.
	 * If the method is call multiple times when the input source has not changed, then
	 * calls made after the first time should have no "effect".
	 * 
	 * With effect, we mean that the <i>when ignoring object identity</i>, the model should
	 * remain the same. It is of course allowed to replace a Method with another Method that is 
	 * structurally identical. Otherwise, it would be necessary to first read the input source
	 * and only replace the original elements in the model if there is a structural change, which
	 * is hard and expensive. 
	 */
	public abstract void refresh() throws ParseException, IOException;
}
