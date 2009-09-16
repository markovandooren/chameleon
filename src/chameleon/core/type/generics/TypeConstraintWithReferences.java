package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class TypeConstraintWithReferences<E extends TypeConstraintWithReferences> extends TypeConstraint<E> {
	public void add(TypeReference tref) {
		if(tref != null) {
			_types.add(tref.parentLink());
		}
	}
	
	public void remove(TypeReference tref) {
		if(tref != null) {
			_types.remove(tref.parentLink());
		}
	}
	
	public List<TypeReference> typeReferences() {
		return _types.getOtherEnds();
	}
	
	private OrderedMultiAssociation<TypeConstraintWithReferences,TypeReference> _types = new OrderedMultiAssociation<TypeConstraintWithReferences, TypeReference>(this);

	public List<Element> children() {
		return new ArrayList<Element>(typeReferences());
	} 
	
	@Override
	public E clone() {
		E result = cloneThis();
		for(TypeReference ref : typeReferences()) {
			result.add(ref.clone());
		}
		return result;
	}
	
	public abstract E cloneThis();

	public Type bound() throws LookupException {
		Iterator<TypeReference> iter = typeReferences().iterator();
		Type result = null;
		if(iter.hasNext()) {
			result = iter.next().getType();
			while(iter.hasNext()) {
				result = result.intersection(iter.next().getType());
			}
		} else {
			throw new LookupException("No type in the extends constraint");
		}
		return result;
	}


}
