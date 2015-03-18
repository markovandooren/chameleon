package org.aikodi.chameleon.workspace;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.input.ParseException;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * An document scanner loads documents from a particular resource into the project.
 * A document scanner create an {@link DocumentLoader} for each document in the
 * resource that it manages. This resource could be an archive file, a directory,
 * a database,...
 * 
 * A document scanner it connected to a {@link View} and adds document loaders
 * to the appropriate {@link Namespace}s within that view.
 * 
 * @author Marko van Dooren
 */
public interface DocumentScanner extends Comparable<DocumentScanner> {

	/**
	 * Return the project populated by this builder.
	 * 
	 * @throws ProjectException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Project project();

	/**
	 * Return the association link that connections this document scanner to its view.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public SingleAssociation<? extends DocumentScanner, ? super DocumentScannerContainer> containerLink();
	
	/**
	 * Return the container of this document scanner.
	 */
	public DocumentScannerContainer container();
	
	/**
	 * Return the view to which this document scanner adds documents.
	 */
 /*@
   @ public behavior
   @
   @ post container() == null ==> \result == null;
   @ post container() != null ==> \result == container().view();
   @*/
	public View view();
	
	/**
	 * Return the document loaders that are managed by this document scanner.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<DocumentLoader> documentLoaders();
	
	/**
	 * Return the number of document loaders.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result == documentLoaders().size();
   @*/
	public int nbDocumentLoaders();
	
	/**
	 * Return the documents that are managed by this document scanner.
	 * This is an expensive operation since all managed documents will be
	 * loaded. 
	 * 
	 * @return The documents that are managed by this document scanner.
	 * @throws InputException A document could not be loaded.
	 */
	public List<Document> documents() throws InputException;
	
	/**
	 * Add the given listener. The added listener will only be notified
	 * of future events. If the listener should also be used to synchronize
	 * a data structure with the collection of current document loader, the
	 * {@link #addAndSynchronizeListener(DocumentLoaderListener)} method should be used.
	 * 
	 * @param listener The listener to be added.
	 */
	public void addListener(DocumentLoaderListener listener);
	
   /**
    * Remove the given listener. The added listener will no longer be notified
    * of future events.
    * 
    * @param listener The listener to be removed.
    */
	public void removeListener(DocumentLoaderListener listener);

	/**
	 * @return the listeners that listen for changes in the collection of
	 *         document loaders.
	 */
	public List<DocumentLoaderListener> documentLoaderListeners();
	
	/**
	 * Add the given listener, and send it an event for every document loader. This
	 * way no separate code must be written to update on a change, and perform the
	 * initial synchronisation.
	 * @param listener
	 */
	public void addAndSynchronizeListener(DocumentLoaderListener listener);

	/**
	 * Return whether this document scanner is reponsible for loading a base library.
	 * A base library is a library that is shipped with a language, and is loaded by
	 * default. It will therefore not be added to configuration files.
	 * 
	 * @return True if this document scanners loads a base libary. False otherwise.
	 */
	public boolean isBaseScanner();
	
	/**
	 * Add the given document loader.
	 * @param source
	 */
 /*@
   @ public behavior
   @
   @ pre source != null;
   @
   @ post documentLoaders().contains(source);
   @ post documentLoaders().containsAll(\old(documentLoaders()));
   @*/
	public void add(DocumentLoader source);
	
	/**
	 * Flush the cache of all documents loaded by this document scanner.
	 */
	public void flushCache();
	
	/**
	 * Apply the given action to all document that are loaded by this document scanner.
	 * @param action The action to be applied.
	 * @throws E
	 * @throws InputException 
	 */
	public <E extends Exception> void apply(Action<? extends Element, E> action) throws E, E, InputException;

	/**
	 * Return the namespaces that are populated by this document scanner.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post 
   @*/
	public List<Namespace> topLevelNamespaces();
	
	public Set<Namespace> namespaces();

	/**
	 * Return a label to describe the resource loaded by this document scanner.
	 * @return
	 */
	public String label();

	/**
	 * DO NOT INVOKE EXTERNALLY. Must be exposed due to ridiculous Java access modifiers.
	 * 
	 * @param container
	 * @throws ProjectException
	 */
	public void notifyContainerConnected(DocumentScannerContainer container) throws ProjectException;

   /**
    * DO NOT INVOKE EXTERNALLY. Must be exposed due to ridiculous Java access modifiers.
    * 
    * @param container
    * @throws ProjectException
    */
	public void notifyContainerRemoved(DocumentScannerContainer project) throws ProjectException;

   /**
    * DO NOT INVOKE EXTERNALLY. Must be exposed due to ridiculous Java access modifiers.
    * 
    * @param container
    * @throws ProjectException
    */
	public void notifyProjectReplaced(DocumentScannerContainer old, DocumentScannerContainer newProject) throws ProjectException;

	/**
	 * Check whether this document scanner scans the same resources as another
	 * document scanner.
	 * 
	 * @param scanner The document scanner for which must be checked whether it
	 *               scans the same resources as this scanner.
	 * @return True if and only if the given document scanner scans the same
	 *         resources as this document scanner.
	 */
	public boolean scansSameAs(DocumentScanner scanner);
	
	/**
	 * Return the top-most scanner in the composite scanner structure.
	 * @return
	 */
	public DocumentScanner rootScanner();

	public void remove(DocumentLoader source);
}
