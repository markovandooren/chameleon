package chameleon.core.type;

import java.util.List;
import java.util.Set;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.type.inheritance.InheritanceRelation;

public abstract class TypeIndirection extends Type {

	public TypeIndirection(SimpleNameSignature sig, Type aliasedType) {
		super(sig);
		_aliasedType = aliasedType;
		setUniParent(aliasedType.getParent());
	}
	
	// @EXTENSIBILITY : change names of constructors?

	public Type aliasedType() {
		return _aliasedType;
	}
	
	private final Type _aliasedType;

	@Override
	public void add(TypeElement element) {
		throw new ChameleonProgrammerException("Trying to add an element to a type alias.");
	}

	@Override
	public Set<TypeElement> directlyDeclaredElements() {
		return (Set<TypeElement>) aliasedType().directlyDeclaredElements();
	}

	@Override
	public void removeInheritanceRelation(InheritanceRelation relation) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a type alias.");
	}



	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to a type alias.");
	}



	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		return aliasedType().inheritanceRelations();
	}

}
