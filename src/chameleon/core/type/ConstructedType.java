package chameleon.core.type;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.type.generics.GenericParameter;

/**
 * This class represents types created as a result of looking up (resolving) a generic parameter, which itself is
 * not a type.
 * 
 * @author Marko van Dooren
 */
public class ConstructedType extends TypeIndirection {

	public ConstructedType(SimpleNameSignature sig, Type aliasedType, GenericParameter param) {
		super(sig, aliasedType);
		_param = param;
	}
	
	public boolean uniEqualTo(Type type) {
		return type == this || 
		       ((type instanceof ConstructedType) && (((ConstructedType)type).parameter().equals(parameter())));
	}
	
	public GenericParameter parameter() {
		return _param;
	}
	
	private final GenericParameter _param;

	@Override
	public ConstructedType clone() {
		return new ConstructedType(signature().clone(), aliasedType(), parameter());
	}

}
