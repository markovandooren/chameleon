package org.aikodi.chameleon.workspace;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespace.Namespace;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * <p>A class representing a source from which a {@link Document} is built. These
 * can be file based (text or binary), editor based, ...</p>
 * 
 * <p>Each document loader manages a single document. There may be multiple
 * document loaders for a single logical document. For example there may be a
 * file document loader and editor buffer document loader for the same logical
 * document. In this case, the {@link #compareTo(DocumentLoader)} method is used
 * to determine which document loader has the highest priority.</p>
 * 
 * <p>A document loader is typically created by a {@link DocumentScanner}. A
 * document scanner scans a particular resource, such as an archive file, a
 * directory, or a database, and create a document loader for each document that
 * it finds.</p>
 * 
 * <p>Every document loader must be attached to a namespace by calling
 * {@link #setNamespace(DocumentLoaderNamespace)}, or
 * {@link DocumentLoaderNamespace#addDocumentLoader(DocumentLoader)}. When that
 * is done, the namespace will invoke {@link #targetDeclarationNames(Namespace)}
 * to obtain the names of the top-level declarations that this loader can
 * provide. The default implementation will simply load the document and inspect
 * it. To implement lazy loading, you must overwrite
 * {@link #targetDeclarationNames(Namespace)} so you can compute the list of
 * names without actually loading anything.</p>
 * 
 * @author Marko van Dooren
 */
public interface DocumentLoader extends Comparable<DocumentLoader> {
	
	/**
	 * Return the list of name of declaration that are added to the given namespace, 
	 * and are visible when the namespace is used as a <b>target</b>. Declarations 
	 * that are only visible lexically from within the namespace are ignored.
	 * @param namespace The namespace for which the requested target declarations
	 *                  are requested.
	 * @return 
	 * @throws InputException 
	 */
	public List<String> targetDeclarationNames(Namespace namespace) throws InputException;
	
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
	 * Load the entire document managed by this loader into the model. 
	 * <b>WARNING</b> The namespace declarations in the returned document
	 * are not connected yet to the corresponding namespaces. The
	 * {@link Document#activate()}  method must be invoked by this method in order
	 * to use the document. The reason for this decision is that it is not
	 * the responsibility of the document loader to decide how the returned document
	 * will be used. It merely creates a document based on the underlying resource.
	 * 
	 * @throws InputException The document could not be loaded. 
	 */
	public Document load() throws InputException;
	
	/**
	 * Refresh the document managed by this document loader. The old document
	 * may modified, or replaced.
	 * 
	 * @return The new document managed by this document loader.
	 * 
	 * @throws InputException the document could not be refreshed.
	 */
	public Document refresh() throws InputException;
	
	/**
	 * @return the namespace in which this document loader loads the document.
	 */
	public Namespace namespace();
	
	/**
	 * Return the association object that connects this document loader to its namespace.
	 * @return
	 */
	public SingleAssociation<DocumentLoader, DocumentLoaderNamespace> namespaceLink();
	
	/**
	 * Return the association object that connects this document loader to the project scanner that created it.
	 * @return
	 */
	public SingleAssociation<DocumentLoader, DocumentScanner> scannerLink();
	
	/**
	 * Return the project to which this document loader belongs.
	 */
 /*@
   @ public behavior
   @
   @ post \result == view().project();
   @*/
	public Project project();
	
	/**
	 * Return the view to which this document loader adds its document.
	 */
 /*@
   @ public behavior
   @
   @ post \result == scanner().view();
   @*/
	public View view();

	/**
	 * Flush the cache of the document that is managed by this document loader.
	 */
	public void flushCache();

	/**
	 * Destroy this document loader, and release any resources.
	 */
	public void destroy();
	
	/**
	 * Attach this document loader to the given namespace. The document
	 * loader will load declarations that are in the given namespace.
	 * 
	 * @param namespace The namespace to which the document loader must be attached.
	 * @throws InputException If the document is loaded immediately, all exceptions
	 *                        that are thrown during the loading will be propagated.
	 */
	public void setNamespace(DocumentLoaderNamespace namespace) throws InputException;
	
	/**
	 * Return the document scanner that created this document loader.
	 * @return
	 */
	public DocumentScanner scanner();
	
	/**
	 * Determine the order of this document loader compared
	 * to a given other document loader.
	 * 
	 * This method is used when there are multiple document
	 * loaders available to load the same declaration. For
	 * example, if a file is opened in a text editor, the
	 * declarations in the file can be loaded from both
	 * the file and the editor buffer. 
	 */
	@Override
	public int compareTo(DocumentLoader o);
}
