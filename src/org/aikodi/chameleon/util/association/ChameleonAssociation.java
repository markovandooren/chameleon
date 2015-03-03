package org.aikodi.chameleon.util.association;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.Verification;

import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;

public interface ChameleonAssociation<T extends Element> extends IAssociation<Element, T> {

//	public void cloneTo(ChameleonAssociation<T> o);
	
	public Verification verify();
	
	public String role();
	
	public void pairWise(ChameleonAssociation<?> other, BiConsumer<Element, Element> consumer);
	
   public void mapTo(ChameleonAssociation<?> other, Function<Element, Element> mapper);

}
