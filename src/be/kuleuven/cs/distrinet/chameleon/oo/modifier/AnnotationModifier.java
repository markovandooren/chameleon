package be.kuleuven.cs.distrinet.chameleon.oo.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

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
