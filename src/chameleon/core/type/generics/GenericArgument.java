package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.type.TypeReference;

public class GenericArgument extends ElementImpl<GenericArgument, Element> {

	public GenericArgument(TypeReference ref) {
		setTypeReference(ref);
	}
	
	@Override
	public GenericArgument clone() {
		return new GenericArgument(typeReference().clone());
	}

	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.add(typeReference());
		return result;
	}
	
	public TypeReference typeReference() {
		return _type.getOtherEnd();
	}
	
	public void setTypeReference(TypeReference ref) {
		if(ref == null) {
			_type.connectTo(null);
		} else {
			_type.connectTo(ref.parentLink());
		}
	}
	
	private Reference<GenericArgument,TypeReference> _type = new Reference<GenericArgument,TypeReference>(this);

}
