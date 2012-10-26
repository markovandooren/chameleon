package chameleon.workspace;

import java.io.IOException;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.input.ParseException;

/**
 * A class representing a source from which a Document is built. These
 * can be file based (text or binary), editor based, ...
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
	 * @throws InputException 
	 */
	public List<String> targetDeclarationNames(Namespace ns) throws InputException;
	
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
	 * <b>WARNING</b> The namespace declaration in the returned document
	 * are not connected yet to the corresponding namespaces. The
	 * {@link Document#activate()} method must be invoked afterwards in order
	 * to use the document. The reason for this decision is that it is not
	 * the reponsibility of the input source to decide how the returned document
	 * will be used. It merely creates a document based on the underlying resource.
	 * 
	 * @throws LookupException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public Document load() throws InputException;
	
//	/**
//	 * Clone this input source.
//	 * @return
//	 */
//	public InputSource clone();
	
	/**
	 * Return the association object that connects this input source to its namespace.
	 * @return
	 */
	public SingleAssociation<InputSource, InputSourceNamespace> namespaceLink();
	
	/**
	 * Return the association object that connects this input source to the project loader that created it.
	 * @return
	 */
	public SingleAssociation<InputSource, DocumentLoader> loaderLink();
	
	/**
	 * Return the project to which this input source belongs.
	 * @return
	 */
	public Project project();
	
	public View view();

	public void flushCache();

	public void destroy();
	
}