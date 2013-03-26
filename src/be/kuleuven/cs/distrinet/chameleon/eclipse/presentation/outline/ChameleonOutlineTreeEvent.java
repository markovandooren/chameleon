package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.outline;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * An event class for a ChameleonOutlineTree
 * It records the element where it acted upon
 *
 */
public class ChameleonOutlineTreeEvent {

	/*
	 * the element that is involved in the event
	 */
	protected Element actedUpon;
	
	/**
	 * Creates a new ChameleonOutlineTreeEvent with the chameleon tree element that is involved
	 * @param involved
	 * 		the chameleon tree element that is involved
	 * 		
	 */
	public ChameleonOutlineTreeEvent(Element involved) {
		actedUpon = involved;
	}
	
	
	/**
	 * 
	 * @return the involved element
	 */
	public Element getInvolved() {
		return actedUpon;
	}
}
