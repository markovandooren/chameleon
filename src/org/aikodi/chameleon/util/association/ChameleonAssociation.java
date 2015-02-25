package org.aikodi.chameleon.util.association;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.Verification;

import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;

public interface ChameleonAssociation<T extends Element> extends IAssociation<Element, T> {

//	public void cloneTo(ChameleonAssociation<T> o);
	
	public Verification verify();
	
	public String role();
	
}
