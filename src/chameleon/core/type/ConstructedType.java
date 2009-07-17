package chameleon.core.type;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.type.generics.FormalGenericParameter;

/**
 * This class represents types created as a result of looking up (resolving) a generic parameter, which itself is
 * not a type.
 * 
 * @author Marko van Dooren
 */
public class ConstructedType extends TypeIndirection {

	public ConstructedType(SimpleNameSignature sig, Type aliasedType, FormalGenericParameter param) {
		super(sig, aliasedType);
		_param = param;
	}
	
	public boolean uniEqualTo(Type type) {
		return type == this || 
		       ((type instanceof ConstructedType) && (((ConstructedType)type).parameter().equals(parameter())));
	}
	
	public FormalGenericParameter parameter() {
		return _param;
	}
	
	private final FormalGenericParameter _param;

	@Override
	public ConstructedType clone() {
		return new ConstructedType(signature().clone(), aliasedType(), parameter());
	}

}
