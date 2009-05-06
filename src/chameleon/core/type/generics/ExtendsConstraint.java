package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.ReferenceSet;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class ExtendsConstraint extends TypeConstraint {

	public ExtendsConstraint() {
	}

	@Override
	public ExtendsConstraint clone() {
		ExtendsConstraint result = new ExtendsConstraint();
		for(TypeReference ref : typeReferences()) {
			result.add(ref.clone());
		}
		return result;
	}

	@Override
	public Type upperBound() throws MetamodelException {
		Iterator<TypeReference> iter = typeReferences().iterator();
		Type result = null;
		if(iter.hasNext()) {
			result = iter.next().getType();
			while(iter.hasNext()) {
				result = result.intersection(iter.next().getType());
			}
		} else {
			throw new MetamodelException("No type in the extends constraint");
		}
		return result;
	}

	@Override
	public boolean matches(Type type) throws MetamodelException {
		return type.subTypeOf(upperBound());
	}

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
	
	private ReferenceSet<ExtendsConstraint,TypeReference> _types = new ReferenceSet<ExtendsConstraint, TypeReference>(this);

	public List<Element> children() {
		return new ArrayList<Element>(typeReferences());
	} 
	
}
