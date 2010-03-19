package chameleon.core.type;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public class TypeAlias extends TypeIndirection {

	
	public TypeAlias(SimpleNameSignature sig, Type aliasedType) {
		super(sig, aliasedType);
		setOrigin(aliasedType);
	}

	@Override
	public boolean uniSameAs(Element type) throws LookupException {
		return super.uniSameAs(type) || 
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
