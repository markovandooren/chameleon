package chameleon.core.type.generics;

import chameleon.core.MetamodelException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class ExtendsConstraint extends TypeConstraint {

	public ExtendsConstraint(TypeReference typeRef) {
		super(typeRef);
	}

	@Override
	public ExtendsConstraint clone() {
		return new ExtendsConstraint(typeReference());
	}

	@Override
	public Type upperBound() throws MetamodelException {
		return typeReference().getType();
	}

	@Override
	public boolean matches(Type type) throws MetamodelException {
		return type.subTypeOf(upperBound());
	}

}
