package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class TypeConstraint<E extends TypeConstraint, P extends GenericParameter> extends ElementImpl<E,P> {

	public TypeConstraint(TypeReference typeRef) {
		setTypeReference(typeRef);
	}
	
	public void setTypeReference(TypeReference typeRef) {
		if(typeRef != null) {
			_typeRef.connectTo(typeRef.getParentLink());
		} else {
			_typeRef.connectTo(null);
		}
	}
	
	public TypeReference typeReference() {
		return _typeRef.getOtherEnd();
	}
	
	public Type type() throws MetamodelException {
		return typeReference().getType();
	}
	
	public abstract boolean matches(Type type) throws MetamodelException;
	
  private Reference<TypeConstraint, TypeReference> _typeRef = new Reference<TypeConstraint, TypeReference>(this);

	@Override
	public abstract E clone();

	public List<? extends Element> getChildren() {
		List<Element> result = new ArrayList<Element>();
		result.add(typeReference());
		return result;
	}

}
