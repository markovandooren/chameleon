package chameleon.core.type.generics;

import java.util.List;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElement;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.exception.ChameleonProgrammerException;

public abstract class WildCardType extends Type {

	public WildCardType(SimpleNameSignature sig, Type upperBound, Type lowerBound) {
		super(sig);
		_upperBound = upperBound;
		_lowerBound = lowerBound;
	}
	
  private Type _upperBound;

  private Type _lowerBound;

  @Override
	public void add(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add an element to a wildcard type.");
	}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from a wildcard type.");
	}

	@Override
	public void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to a wildcard type.");
	}

	@Override
	public void addParameter(TypeParameter parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a wildcard type.");
	}

	public Type upperBound() {
		return _upperBound;
	}
	
	public Type lowerBound() {
		return _upperBound;
	}
	
	public Type baseType() {
		return this;
	}

	@Override
	public ExtendsWildcardType clone() {
		return new ExtendsWildcardType(_upperBound);
	}

	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		return upperBound().directlyDeclaredElements();
	}

	@Override
	public List<Member> directlyDeclaredMembers() {
		return upperBound().directlyDeclaredMembers();
	}

	@Override
	public <D extends Member> List<D> directlyDeclaredMembers(DeclarationSelector<D> selector) throws LookupException {
		return upperBound().directlyDeclaredMembers(selector);
	}

	@Override
	public List<TypeParameter> parameters() {
		return upperBound().parameters();
	}

	@Override
	public void removeInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove a super type from a type alias.");
	}

	@Override
	public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
	}

	@Override
	public void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}
	
	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		return upperBound().inheritanceRelations();
	}
	

}
