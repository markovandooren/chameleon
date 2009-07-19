package chameleon.core.type;

import chameleon.core.declaration.SimpleNameSignature;

public class DerivedType extends RegularType {

	public DerivedType(Type type) {
		super(type.signature().clone());
		copyContents(type);
		_baseType = type;
	}

	private Type _baseType;
	
	@Override
	public Type baseType() {
		return _baseType;
	}

}
