package org.aikodi.chameleon.eclipse.view.outline;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * An implementation for the chameleonTree that does absolutely nothing.
 * Events are ignored
 * This is used in case that a chameleon tree has no listeners
 */
public class NullChameleonOutlineTreeListener implements IChameleonOutlineTreeListener {

	private static NullChameleonOutlineTreeListener soleInstance = new NullChameleonOutlineTreeListener();
	public static NullChameleonOutlineTreeListener getSoleInstance() {
		return soleInstance;
	}
	
	@Override
   public void add(ChameleonOutlineTreeEvent event){		
	}
	
	@Override
   public void remove(ChameleonOutlineTreeEvent event){	
	}

	@Override
   public void fireChanged() {
	}

}
