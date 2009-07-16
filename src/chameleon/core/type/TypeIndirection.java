package chameleon.core.type;

import java.util.List;

import org.rejuse.property.PropertySet;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.type.inheritance.InheritanceRelation;

public abstract class TypeIndirection extends Type {

	public TypeIndirection(SimpleNameSignature sig, Type aliasedType) {
		super(sig);
		_aliasedType = aliasedType;
		setUniParent(aliasedType.parent());
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
	public List<Member> directlyDeclaredElements() {
		return aliasedType().directlyDeclaredElements();
	}
	
	@Override
	public <D extends Member> List<D> directlyDeclaredElements(DeclarationSelector<D> selector) throws LookupException {
		return aliasedType().directlyDeclaredElements(selector);
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
	
	//TODO I am not sure if these definitions are appropriate for a constructed type.
  public PropertySet<Element> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedType().defaultProperties());
  }
	
  public PropertySet<Element> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedType().declaredProperties());
  }

  public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
  }

}
