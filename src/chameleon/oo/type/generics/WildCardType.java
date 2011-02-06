package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.AbstractType;
import chameleon.oo.type.Parameter;
import chameleon.oo.type.ParameterBlock;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElement;
import chameleon.oo.type.inheritance.AbstractInheritanceRelation;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Pair;
import chameleon.util.Util;

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

	public <P extends Parameter> void addParameter(Class<P> kind, P parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a wildcard type.");
	}

	public Type upperBound() {
		return _upperBound;
	}
	
	public Type lowerBound() {
		return _lowerBound;
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
	public <P extends Parameter> List<P> parameters(Class<P> kind) {
		return upperBound().parameters(kind);
	}

	@Override
	public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
		return upperBound().nbTypeParameters(kind);
	}
	
	@Override
	public <P extends Parameter> P parameter(Class<P> kind,int index) {
		return upperBound().parameter(kind,index);
	}

	@Override
	public void removeNonMemberInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove a super type from a wildcard type.");
	}

	@Override
	public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a type alias.");
	}

	public <P extends Parameter> void replaceParameter(Class<P> kind, P oldParameter, P newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}
	
	public <P extends Parameter> void replaceAllParameter(Class<P> kind, List<P> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a type alias.");
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() throws LookupException {
		//FIXME wrong!
		return upperBound().inheritanceRelations();
	}

	@Override
	public List<InheritanceRelation> nonMemberInheritanceRelations() {
		//FIXME wrong!
		return upperBound().nonMemberInheritanceRelations();
	}

	public List<Type> getDirectSuperTypes() throws LookupException {
//	return aliasedType().getDirectSuperTypes();
	  return Util.createNonNullList(upperBound());
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
	
	public void addParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to add a parameter block to a type alias.");
	}

	public Class<? extends Parameter> kindOf(ParameterBlock block) throws LookupException {
		return null;
	}

	public <P extends Parameter> ParameterBlock<?, P> parameterBlock(Class<P> kind) {
		return null;
	}

	public List<ParameterBlock> parameterBlocks() {
		return new ArrayList<ParameterBlock>();
	}

	public void removeParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to remove a parameter block to a type alias.");
	}

}
