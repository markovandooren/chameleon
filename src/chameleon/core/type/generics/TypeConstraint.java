package chameleon.core.type.generics;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public abstract class TypeConstraint<E extends TypeConstraint, P extends Element> extends ElementImpl<E,P> {

	public TypeConstraint() {
//		setTypeReference(typeRef);
	}
	
//	public void setTypeReference(TypeReference typeRef) {
//		if(typeRef != null) {
//			_typeRef.connectTo(typeRef.parentLink());
//		} else {
//			_typeRef.connectTo(null);
//		}
//	}
//	
//	public TypeReference typeReference() {
//		return _typeRef.getOtherEnd();
//	}
//	
//	public Type type() throws MetamodelException {
//		return typeReference().getType();
//	}
	
	public abstract boolean matches(Type type) throws LookupException;
	
  private Reference<TypeConstraint, TypeReference> _typeRef = new Reference<TypeConstraint, TypeReference>(this);

	@Override
	public abstract E clone();

//	public List<? extends Element> children() {
//		List<Element> result = new ArrayList<Element>();
//		result.add(typeReference());
//		return result;
//	}

	/**
	 * Return the lower bound on the type that this type constraint imposes.
	 * 
	 * @return
	 * @throws LookupException 
	 */
	public abstract Type upperBound() throws LookupException;
	
}
