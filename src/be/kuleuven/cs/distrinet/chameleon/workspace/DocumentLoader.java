package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.IOException;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;



/**
 * An abstract super class for creating projects.
 * 
 * @author Marko van Dooren
 */
public interface DocumentLoader {

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

	public Association<? extends DocumentLoader, ? super View> viewLink();
	
	public View view();
	
	/**
	 * Return the input sources that are managed by this document loader.
	 * @return
	 */
	public List<InputSource> inputSources();
	
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
	 * Return whether this document loader is reponsible for loading a base library.
	 * A base library is a library that is shipped with a language, and is loaded by
	 * default. It will therefore not be added to configuration files.
	 * 
	 * @return True if this document loaders loads a base libary. False otherwise.
	 */
	public boolean isBaseLoader();
	
	/**
	 * Flush the cache of all documents loaded by this document loader.
	 */
	public void flushCache();
}
