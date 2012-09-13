package chameleon.workspace;

import java.io.IOException;
import java.util.List;

import chameleon.core.declaration.Declaration;
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
	public void load() throws InputException;
	
}