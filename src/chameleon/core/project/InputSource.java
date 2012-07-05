package chameleon.core.project;

import java.io.IOException;
import java.util.Map;

import chameleon.core.declaration.Declaration;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.RootNamespace;
import chameleon.exception.ChameleonProgrammerException;
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
   @ pre targetNamespace != null;
   @
   @ post targetNamespace() == defaultNamespace;
   @*/
	public InputSource(RootNamespace targetNamespace) {
		if(targetNamespace == null) {
			throw new ChameleonProgrammerException();
		}
		_targetNamespace = targetNamespace;
	}

	private Namespace _targetNamespace;
	
	/**
	 * Return the root namespace in which the elements from this input
	 * source are put.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Namespace targetNamespace() {
		return _targetNamespace;
	}
	
	/**
	 * Perform a clean read of the input. This method is called when the model
	 * is built initially or when the underlying input source is modified. It is
	 * the responsibility of the implementation to ensure that a refresh is idempotent.
	 * If the method is call multiple times when the input source has not changed, then
	 * calls made after the first time should have no "effect".
	 * 
	 * With effect, we mean that <i>when ignoring object identity</i>, the model should
	 * remain the same. It is of course allowed to replace for examplea Method with another Method that is 
	 * structurally identical. Otherwise, it would be necessary to first read the input source
	 * and only replace the original elements in the model if there is a structural change, which
	 * is hard and expensive.
	 */
	public abstract void refresh() throws ParseException, IOException;
	
	/**
	 * Return a map that contains the top level declarations that can be found in this input source.
	 * The resulting map maps simple names onto the type of declaration.
	 * 
	 * @return
	 */
	public abstract Map<String, Class<? extends Declaration>> candidates();
}