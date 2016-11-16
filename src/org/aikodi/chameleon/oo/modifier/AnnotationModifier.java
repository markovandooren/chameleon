package org.aikodi.chameleon.oo.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.rejuse.property.PropertySet;

public class AnnotationModifier extends ModifierImpl {

	public AnnotationModifier(TypeReference tref) {
		setTypeReference(tref);
	}
	
	private AnnotationModifier() {}
	
	@Override
	protected AnnotationModifier cloneSelf() {
		return new AnnotationModifier();
	}

	private Single<TypeReference> _typeReference = new Single<>(this);
	
	public TypeReference typeReference() {
		return _typeReference.getOtherEnd();
	}
	
	public Type type() throws LookupException {
		return typeReference().getElement();
	}
	
	public void setTypeReference(TypeReference tref) {
		set(_typeReference,tref);
	}

	@Override
   public PropertySet<Element, ChameleonProperty> impliedProperties() {
		return new PropertySet<Element, ChameleonProperty>();
	}
}
