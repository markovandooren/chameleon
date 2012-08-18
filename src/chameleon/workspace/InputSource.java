package chameleon.workspace;

import java.io.IOException;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.input.ParseException;

/**
 * A class representing a source from which a Document is built. These
 * can be file based, editor based, ...
 * 
 * @author Marko van Dooren
 */
public interface InputSource {
	
//	/**
//	 * Create a new input source that puts its elements in the given
//	 * default namespace.
//	 */
// /*@
//   @ public behavior
//   @
//   @ pre targetNamespace != null;
//   @
//   @ post targetNamespace() == defaultNamespace;
//   @*/
//	public InputSource(//RootNamespace targetNamespace
//			) {
////		if(targetNamespace == null) {
////			throw new ChameleonProgrammerException();
////		}
////		_targetNamespace = targetNamespace;
//	}

//	private Namespace _targetNamespace;
	
//	/**
//	 * Return the root namespace in which the elements from this input
//	 * source are put.
//	 */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//	public Namespace targetNamespace() {
//		return _targetNamespace;
//	}
	
//	/**
//	 * Perform a clean read of the input. This method is called when the model
//	 * is built initially or when the underlying input source is modified. It is
//	 * the responsibility of the implementation to ensure that a refresh is idempotent.
//	 * If the method is call multiple times when the input source has not changed, then
//	 * calls made after the first time should have no "effect".
//	 * 
//	 * With effect, we mean that <i>when ignoring object identity</i>, the model should
//	 * remain the same. It is of course allowed to replace for example a Method with another Method that is 
//	 * structurally identical. Otherwise, it would be necessary to first read the input source
//	 * and only replace the original elements in the model if there is a structural change, which
//	 * is hard and expensive.
//	 */
//	public abstract void refresh() throws ParseException, IOException;
	
	/**
	 * Return a map that contains the top level declarations that can be found in this input source.
	 * The resulting map maps simple names onto the type of declaration.
	 * 
	 * @return
	 */
//	public abstract Map<String, Class<? extends Declaration>> candidates();
	
	/**
	 * Return the list of name of declaration that are added to the given namespace, and
	 * are visible when the namespace is used as a <b>target</b>. Declarations that are only visible lexically from
	 * within the namespace are ignored.
	 * @param ns
	 * @return
	 */
	public List<String> targetDeclarationNames(Namespace ns);
	
	/**
	 * Load the declarations with the given name into the model (if any) and return them in a list.
	 * 
	 * This method may also load additional elements into the model, but they are not elements of
	 * the returned list.
	 * 
	 * @param name The name of the declarations that must be loaded.
	 * @return
	 * @throws LookupException 
	 */
	public abstract List<Declaration> targetDeclarations(String name) throws LookupException;
	
	/**
	 * Load the entire source managed by this InputSource into the model.
	 * @throws LookupException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void load() throws IOException, ParseException;
	
	public Document document();
}