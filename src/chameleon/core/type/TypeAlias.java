package chameleon.core.type;

import java.util.List;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.type.generics.TypeParameter;

public class TypeAlias extends TypeIndirection {

	
	public TypeAlias(SimpleNameSignature sig, Type aliasedType) {
		super(sig, aliasedType);
	}

	public boolean uniEqualTo(Type type) {
		return super.uniEqualTo(type) || 
		       type.equals(aliasedType()) || 
		       ((type instanceof TypeIndirection) && (((TypeIndirection)type).aliasedType().equals(aliasedType())));
	}
	
	/**
	 * OVERRIDE IN SUBCLASSES !!!!
	 */
	public TypeAlias clone() {
		return new TypeAlias(signature().clone(), aliasedType());
	}

}
