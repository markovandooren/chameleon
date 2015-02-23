package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.IOException;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * A class representing a source from which a Document is built. These
 * can be file based (text or binary), editor based, ...
 * 
 * Each input source manages a single document. There may be multiple
 * input sources for a single logical document. For example there
 * may be a file input source and editor buffer input source for
 * the same logical document. In this case, the {@link #compareTo(InputSource)} method
 * is used to determine which input source has the highest priority.
 * 
 * @author Marko van Dooren
 */
public interface InputSource extends Comparable<InputSource> {
	
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
	 * Load the entire document managed by this InputSource into the model. 
	 * <b>WARNING</b> The namespace declarations in the returned document
	 * are not connected yet to the corresponding namespaces. The
	 * {@link Document#activate()}  method must be invoked by this method in order
	 * to use the document. The reason for this decision is that it is not
	 * the responsibility of the input source to decide how the returned document
	 * will be used. It merely creates a document based on the underlying resource.
	 * 
	 * @throws LookupException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public Document load() throws InputException;
	
	/**
	 * Refresh the document managed by this input source. The old document
	 * may modified, or replaced.
	 * 
	 * @return The new document managed by this input source.
	 * 
	 * @throws InputException the document could not be refreshed.
	 */
	public Document refresh() throws InputException;
	
	/**
	 * @return the namespace in which this input source loads the document.
	 */
	public Namespace namespace();
	
	/**
	 * Return the association object that connects this input source to its namespace.
	 * @return
	 */
	public SingleAssociation<InputSource, InputSourceNamespace> namespaceLink();
	
	/**
	 * Return the association object that connects this input source to the project loader that created it.
	 * @return
	 */
	public SingleAssociation<InputSource, DocumentScanner> scannerLink();
	
	/**
	 * Return the project to which this input source belongs.
	 */
 /*@
   @ public behavior
   @
   @ post \result == view().project();
   @*/
	public Project project();
	
	/**
	 * Return the view to which this input source adds its document.
	 */
 /*@
   @ public behavior
   @
   @ post \result == loader().view();
   @*/
	public View view();

	/**
	 * Flush the cache of the document that is managed by this input source.
	 */
	public void flushCache();

	/**
	 * Destroy this input source, and release any resources.
	 */
	public void destroy();
	
	/**
	 * Attach this input source to the given namespace. The input
	 * source will load declarations that are in the given namespace.
	 * 
	 * @param namespace The namespace to which the input source must be attached.
	 * @throws InputException If the document is loaded immediately, all exceptions
	 *                        that are thrown during the loading will be propagated.
	 */
	public void setNamespace(InputSourceNamespace namespace) throws InputException;
	
	/**
	 * Return the document loader that created this input source.
	 * @return
	 */
	public DocumentScanner loader();
	
	/**
	 * Determine the order of this input source compared
	 * to a given other input source.
	 * 
	 * This method is used when there are multiple input
	 * sources available to load the same declaration. For
	 * example, if a file is opened in a text editor, the
	 * declarations in the file can be loaded from both
	 * the file and the editor buffer. 
	 */
	@Override
	public int compareTo(InputSource o);
}
