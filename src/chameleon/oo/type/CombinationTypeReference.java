package chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;

public abstract class CombinationTypeReference extends NamespaceElementImpl implements TypeReference {

	public abstract CombinationTypeReference clone();

	private OrderedMultiAssociation<CombinationTypeReference,TypeReference> _types = new OrderedMultiAssociation<CombinationTypeReference, TypeReference>(this);
	
	public CombinationTypeReference() {
		super();
	}

	public List<TypeReference> typeReferences() {
		return _types.getOtherEnds();
	}

	public TypeReference elementAt(int baseOneIndex) {
		return _types.elementAt(baseOneIndex);
	}
	
	public List<Element> children() {
		return new ArrayList<Element>(typeReferences());
	}

	public Type getType() throws LookupException {
		return getElement();
	}

	public void add(TypeReference tref) {
		add(_types,tref);
	}

	public void addAll(List<? extends TypeReference> refs) {
		for(TypeReference ref: refs) {
			add(ref);
		}
	}

	public void remove(TypeReference tref) {
		remove(_types,tref);
	}

	@Override
	public String toString() {
		List<? extends TypeReference> trefs = typeReferences();
		int size = trefs.size();
		StringBuffer result = new StringBuffer();
		for(int i=0; i< size; i++) {
			result.append(trefs.get(i).toString());
			if(i<size - 1) {
				result.append(operatorName());
			}
		}
		return result.toString();
	}

	public abstract String operatorName();
	
  public LocalLookupStrategy<?> targetContext() throws LookupException {
  	return getElement().targetContext();
  }
}