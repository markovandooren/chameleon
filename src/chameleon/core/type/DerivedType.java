package chameleon.core.type;

import chameleon.core.declaration.SimpleNameSignature;

public class DerivedType extends RegularType {

	public DerivedType(Type type) {
		super(type.signature().clone());
		// Does not take nested members of any kind into account.
		copyContents(type, true);
		_baseType = type;
		setOrigin(type);
	}

	private Type _baseType;
	
	@Override
	public Type baseType() {
		return _baseType;
	}

}
