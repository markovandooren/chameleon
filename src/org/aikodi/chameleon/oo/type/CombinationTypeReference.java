package org.aikodi.chameleon.oo.type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.util.association.Multi;

public abstract class CombinationTypeReference extends ElementImpl implements TypeReference {

//	public abstract CombinationTypeReference clone();

	private Multi<TypeReference> _types = new Multi<TypeReference>(this);
	
	public CombinationTypeReference() {
		super();
	}

	public List<TypeReference> typeReferences() {
		return _types.getOtherEnds();
	}

	public TypeReference elementAt(int index) {
		return _types.elementAt(index);
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

	public String toString() {
		return toString(new HashSet<>());
	}
	
	@Override
	public String toString(Set<Element> visited) {
		List<? extends TypeReference> trefs = typeReferences();
		int size = trefs.size();
		StringBuilder result = new StringBuilder();
		for(int i=0; i< size; i++) {
			result.append(trefs.get(i).toString(visited));
			if(i<size - 1) {
				result.append(operatorName());
			}
		}
		return result.toString();
	}

	public abstract String operatorName();
	
}
