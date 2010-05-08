package chameleon.oo.type;

import java.util.List;

import org.rejuse.property.PropertySet;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.property.ChameleonProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.oo.type.inheritance.InheritanceRelation;

public abstract class TypeIndirection extends AbstractType {

	public TypeIndirection(SimpleNameSignature sig, Type aliasedType) {
		super(sig);
		_aliasedType = aliasedType;
		if(aliasedType != null) {
		  setUniParent(aliasedType.parent());
		}
	}
	
	// @EXTENSIBILITY : change names of constructors?

	public Type aliasedType() {
		return _aliasedType;
	}
	
	protected void setAliasedType(Type type) {
		_aliasedType = type;
	}
	
	private Type _aliasedType;

	@Override
	public void add(TypeElement element) {
		throw new ChameleonProgrammerException("Trying to add an element to a type alias.");
	}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from a type alias.");
	}

	@Override
	public List<Member> localMembers() throws LookupException {
		return aliasedType().localMembers();
	}
	
	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
		return aliasedType().localMembers(selector);
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
  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedType().defaultProperties());
  }
	
  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return filterProperties(myDeclaredProperties(), aliasedType().declaredProperties());
  }

  public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
  }
  
	@Override
	public Type baseType() {
		return aliasedType().baseType();
	}

	@Override
	public List<TypeParameter> parameters() {
		return aliasedType().parameters();
	}
	
	@Override
	public TypeParameter parameter(int index) {
		return aliasedType().parameter(index);
	}

	public int nbTypeParameters() {
		return aliasedType().nbTypeParameters();
	}

	@Override
	public void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}

	@Override
	public void replaceAllParameter(List<TypeParameter> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a type alias.");
	}

	@Override
	public void addParameter(TypeParameter parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a type alias.");
	}
  
	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		return aliasedType().directlyDeclaredElements();
	}

	public Type lowerBound() {
		return aliasedType().lowerBound();
	}
	
	public Type upperBound() {
		return aliasedType().upperBound();
	}
}
