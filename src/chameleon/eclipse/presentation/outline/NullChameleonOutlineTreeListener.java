package chameleon.eclipse.presentation.outline;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * An implementation for the chameleonTree that does absolutely nothing.
 * Events are ignored
 * This is used in case that a chameleon tree has no listeners
 */
public class NullChameleonOutlineTreeListener implements IChameleonOutlineTreeListener {

	protected static NullChameleonOutlineTreeListener soleInstance = new NullChameleonOutlineTreeListener();
	public static NullChameleonOutlineTreeListener getSoleInstance() {
		return soleInstance;
	}
	
	public void add(ChameleonOutlineTreeEvent event){		
	}
	
	public void remove(ChameleonOutlineTreeEvent event){	
	}

	public void fireChanged() {
	}

}
