package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * An document scanner loads documents from a particular resource into the project.
 * A document scanner create an {@link InputSource} for each document in the
 * resource that it manages. This resource could be a jar file, a directory,
 * a database,...
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
	 * Return the input sources that are managed by this document scanner.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<InputSource> inputSources();
	
	/**
	 * Return the number of input sources.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result == inputSources().size();
   @*/
	public int nbInputSources();
	
	public List<Document> documents() throws InputException;
	
	/**
	 * Add the given listener. The added listener will only be notified
	 * of future events. If the listener should also be used to synchronize
	 * a data structure with the collection of current input source, the
	 * {@link #addAndSynchronizeListener(InputSourceListener)} method should be used. 
	 * @param listener
	 */
	public void addListener(InputSourceListener listener);
	
	public void removeListener(InputSourceListener listener);

	public List<InputSourceListener> inputSourceListeners();
	
	/**
	 * Add the given listener, and send it an event for every input source. This
	 * way no separate code must be written to update on a change, and perform the
	 * initial synchronisation.
	 * @param listener
	 */
	public void addAndSynchronizeListener(InputSourceListener listener);

	/**
	 * Return whether this document scanner is reponsible for loading a base library.
	 * A base library is a library that is shipped with a language, and is loaded by
	 * default. It will therefore not be added to configuration files.
	 * 
	 * @return True if this document scanners loads a base libary. False otherwise.
	 */
	public boolean isBaseScanner();
	
	/**
	 * Add the given input source.
	 * @param source
	 */
 /*@
   @ public behavior
   @
   @ pre source != null;
   @
   @ post inputSources().contains(source);
   @ post inputSources().containsAll(\old(inputSources()));
   @*/
	public void addInputSource(InputSource source);
	
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

}
