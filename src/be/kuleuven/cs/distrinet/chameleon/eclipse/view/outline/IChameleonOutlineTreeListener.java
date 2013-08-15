package be.kuleuven.cs.distrinet.chameleon.eclipse.view.outline;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A listener for tree events
 */
public interface IChameleonOutlineTreeListener {
	
	/**
	 * An element-added event was called
	 * @param event
	 * 		The event 
	 */
	public void add(ChameleonOutlineTreeEvent event);
	
	/**
	 * An element-removed event was called
	 * @param event
	 * 		The event
	 */
	public void remove(ChameleonOutlineTreeEvent event);
	
	/**
	 * The chameleonTree is changed
	 * Implementing listeners should take proper actions
	 */
	public void fireChanged();
}
