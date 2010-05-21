package chameleon.oo.type.generics;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.AbstractType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElement;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Pair;

public abstract class WildCardType extends AbstractType {

	public WildCardType(SimpleNameSignature sig, Type upperBound, Type lowerBound) {
		super(sig);
		_upperBound = upperBound;
		_lowerBound = lowerBound;
	}
	
	public abstract String getFullyQualifiedName();
	
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
	public List<Member> localMembers() throws LookupException {
		return upperBound().localMembers();
	}

	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
		return upperBound().localMembers(selector);
	}

	//FIXME shouldn't this return an empty list?
	@Override
	public List<TypeParameter> parameters() {
		return upperBound().parameters();
	}

	@Override
	public int nbTypeParameters() {
		return upperBound().nbTypeParameters();
	}
	
	@Override
	public TypeParameter parameter(int index) {
		return upperBound().parameter(index);
	}

	@Override
	public void removeInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove a super type from a wildcard type.");
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
	public void replaceAllParameter(List<TypeParameter> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a type alias.");
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		return upperBound().inheritanceRelations();
	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		if(other instanceof WildCardType) {
			WildCardType wild = (WildCardType) other;
			return upperBound().sameAs(wild.upperBound()) && lowerBound().sameAs(wild.lowerBound());
		} else {
			return false;
		}
	}
	
	public boolean uniSameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		if(other instanceof WildCardType) {
			WildCardType wild = (WildCardType) other;
			return upperBound().sameAs(wild.upperBound(),trace) && lowerBound().sameAs(wild.lowerBound(),trace);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return lowerBound().hashCode()+upperBound().hashCode();
	}

	public Declaration declarator() {
		return this;
	}
}
