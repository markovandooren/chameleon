package chameleon.oo.type;

import java.util.List;

import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.util.association.Multi;

public abstract class CombinationTypeReference extends ElementImpl implements TypeReference {

	public abstract CombinationTypeReference clone();

	private Multi<TypeReference> _types = new Multi<TypeReference>(this);
	
	public CombinationTypeReference() {
		super();
	}

	public List<TypeReference> typeReferences() {
		return _types.getOtherEnds();
	}

	public TypeReference elementAt(int baseOneIndex) {
		return _types.elementAt(baseOneIndex);
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