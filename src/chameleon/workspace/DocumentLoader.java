package chameleon.workspace;

import java.io.IOException;
import java.util.List;

import org.rejuse.association.Association;

import chameleon.input.ParseException;



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

	public void flushCache();
}
