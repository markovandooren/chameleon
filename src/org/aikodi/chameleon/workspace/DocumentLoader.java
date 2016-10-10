package org.aikodi.chameleon.workspace;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * <p>
 * A class representing a source from which a {@link Document} is built. These
 * can be file based (text or binary), editor based, ...
 * </p>
 * 
 * <h3>Class Diagram</h3>
 * 
 * <embed src="documentLoader-class.svg"/>
 * 
 * <h3>Object Structure</h3>
 * 
 * <embed src="loader.svg"/>
 *
 * <p>
 * A document loader is typically created by a {@link DocumentScanner}. A
 * document scanner scans a particular resource, such as an archive file, a
 * directory, or a database, and create a document loader for each document that
 * can potentially be loaded. 
 * </p>
 * 
 * <h3>Connecting to a namespace</h3>
 * 
 * <p>The framework does not automatically call {@link #setNamespace(DocumentLoaderNamespace)}
 * because it should only be done after the loader has been initialized properly.
 * Connecting the loader may trigger loading of the document, which is not
 * possible if the initialization of the loader has not finished.</p> 
 * <h3>Lazy Loading</h3>
 * 
 *  <p>The default implementation of {@link #refreshTargetDeclarationNames(Namespace)}
 *  will simply load the document and inspect it. To implement lazy loading, 
 *  you must overwrite {@link #refreshTargetDeclarationNames(Namespace)} to compute the
 *  list of names without actually loading the document.</p>
 * 
 * <h3>Multiple loaders per document</h3>
 * 
 * <p>
 * Each document loader manages a single {@link Document} object, and each {@link
 * Document} is loaded by a single document loader. But there may multiple document 
 * loaders for a single <b>real world</b> document. For example there may be a
 * file document loader and an editor buffer document loader for the same file
 * (real world document). The {@link #compareTo(DocumentLoader)} method is used
 * to determine which document loader has the highest priority.
 * </p>
 * 
 * @author Marko van Dooren
 */
/*
@startuml documentLoader-class.svg
  interface DocumentScanner [[DocumentScanner.html]]
  interface DocumentLoader {
  +project() 
  +view()
  +scanner() 
  +scannerLink()
  .. loading .. 
  +load()
  +refresh()
  +setNamespace(DocumentLoaderNamespace)
  +targetDeclarationNames(Namespace)
  +targetDeclarations(String)
}
interface Document [[../core/document/Document.html]]
interface Namespace [[../core/namespace/Namespace.html]]
DocumentScanner - DocumentLoader
DocumentLoader - Document
Namespace -- DocumentLoader  
@enduml

 @startuml loader.svg
 left to right direction
 object namespace
 object documentLoader
 object document
 object documentScanner
 object project
 object view
 
 project -- view
 view -- documentScanner
 documentScanner -- documentLoader
 documentLoader -- document
 namespace -- documentLoader
 @enduml

@startuml namespace-interaction.svg
[-> documentLoader: setNamespace(namespace)
activate documentLoader
documentLoader -> namespace : addDocumentLoader(this)
activate namespace
namespace -> documentLoader : targetDeclarationNames(this)
activate documentLoader
documentLoader --> namespace : names
deactivate documentLoader
loop name in names
namespace -> map : get(name)
activate map
map --> namespace : queue
deactivate map
namespace -> queue : add(documentLoader)
activate queue
queue -> documentLoader : compareTo(?)
activate documentLoader
documentLoader --> queue
deactivate documentLoader
queue --> namespace
deactivate queue
end
namespace --> documentLoader
deactivate namespace 
[<-- documentLoader
deactivate documentLoader
@enduml
 */
public interface DocumentLoader extends Comparable<DocumentLoader> {
	
	public final static String LoggerName = "org.aikodi.chameleon.io.documentloader";
	public final static Logger Logger = LogManager.getLogger(LoggerName);
	
	/**
	 * <p>Return the list of name of declaration that are added to the given namespace, 
	 * and are visible when the namespace is used as a <b>target</b>. Declarations 
	 * that are only visible lexically from within the namespace are ignored.</p>
	 * 
	 * <p>The result of this method is the same as the of {@link #refreshTargetDeclarationNames(Namespace)}.
	 * The difference is that this method is <em>allowed</em> to perform I/O (e.g.
	 * by loading the document), while {@link #refreshTargetDeclarationNames(Namespace)}
	 * cannot do so. This method is used when the names are requested initially.
	 * {@link #refreshTargetDeclarationNames(Namespace)} is used to update
	 * the information afterwards.</p>
	 * 
   * <p>The default implementation loads simply returns
   * {@link #refreshTargetDeclarationNames(Namespace)}.</p>
   * 
	 * @param namespace The namespace for which the requested target declarations
	 *                  are requested.
	 * @return The value of {@link #refreshTargetDeclarationNames(Namespace)}
	 * @throws InputException 
   */
  public default List<String> targetDeclarationNames(Namespace ns) throws InputException {
    return refreshTargetDeclarationNames(ns);
  }
  
	
	/**
   * <p>Return the list of name of declaration that are added to the given namespace, 
   * and are visible when the namespace is used as a <b>target</b>. Declarations 
   * that are only visible lexically from within the namespace are ignored.</p>
   * 
   * @param namespace The namespace for which the requested target declarations
   *                  are requested.
   * @return
   */
  public List<String> refreshTargetDeclarationNames(Namespace namespace);

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
   * <p>
   * Attach this document loader to the given namespace. The document loader
   * will load declarations that are in the given namespace.
   * </p>
   * 
   * <p>
   * When a document loader is added to a namespace via
   * {@link #setNamespace(DocumentLoaderNamespace)}:
   * <ol>
   * <li>the loader invokes
   * {@link DocumentLoaderNamespace#addDocumentLoader(DocumentLoader)} on the
   * namespace.
   * <li>the namespace will set up the bidirectional association (not shown on
   * the diagram because the {@link Association} objects are involved).</li>
   * <li>calls {@link #targetDeclarationNames(Namespace)} to find out which
   * top-level declarations might be loaded by this loader.</li>
   * <li>the document loader returns a list of names</li>
   * <li>for each name in the list:</li>
   * <ol>
   * <li>the namespace looks for the queue of loaders that load the name</li>
   * <li>the namespace add the document loader to the queue.</li>
   * <li>during this operation, the queue uses the
   * {@link #compareTo(DocumentLoader)} method to decide which loader get
   * priority. (see the section on multiple loaders)</li>
   * </ol>
   * </p>
   * 
   * <embed src="namespace-interaction.svg"/>
   * 
   * @param namespace
   *          The namespace to which the document loader must be attached.
   * @throws InputException
   *           If the document is loaded immediately, all exceptions that are
   *           thrown during the loading will be propagated.
   */
	public void setNamespace(DocumentLoaderNamespace namespace) throws InputException;
	
	/**
	 * Return the document scanner that created this document loader.
	 * 
	 * @return The result is not null.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
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
	
	/**
	 * Return the total amount of time this document loader spent loading its document.
	 * Note that the result is not necessarily the time it took to load the
	 * document once. If the document was garbage collected, it may have to load the
	 * document again in a later stage.
	 * 
	 * @return The number of nanoseconds this document loader spent loading documents.
	 *         The result is not negative.
	 */
	public long loadTime();

	/**
	 * Return the number of times this document loader loaded its document.
	 * If the document is garbage collected, the loader may have to load
	 * it again at a later time.
	 * 
	 * @return The number of times this document loader loaded its document.
	 *         The result is not negative.
	 */
	public int numberOfLoads();
	
}
