package org.aikodi.chameleon.oo.type.generics;

import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.type.ClassImpl;
import org.aikodi.chameleon.oo.type.Parameter;
import org.aikodi.chameleon.oo.type.ParameterBlock;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeElement;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Pair;
import org.aikodi.chameleon.util.Util;

import com.google.common.collect.ImmutableList;

public abstract class WildCardType extends ClassImpl {

	public WildCardType(String name, Type upperBound, Type lowerBound) {
		super(name);
		_upperBound = upperBound;
		_lowerBound = lowerBound;
	}
	
	@Override
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
   public <P extends Parameter> void addParameter(Class<P> kind, P parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a wildcard type.");
	}

	@Override
   public Type upperBound() {
		return _upperBound;
	}
	
	@Override
   public Type lowerBound() {
		return _lowerBound;
	}
	
	@Override
   public Type baseType() {
		return this;
	}

	@Override
	public ExtendsWildcardType cloneSelf() {
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
	public <D extends Member> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException {
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

	@Override
   public <P extends Parameter> void replaceParameter(Class<P> kind, P oldParameter, P newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a type alias.");
	}
	
	@Override
   public <P extends Parameter> void replaceAllParameters(Class<P> kind, List<P> newParameters) {
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

  @Override
  public List<InheritanceRelation> explicitNonMemberInheritanceRelations() {
  	return nonMemberInheritanceRelations();
  }

	
	@Override
   public List<Type> getDirectSuperTypes() throws LookupException {
//	return aliasedType().getDirectSuperTypes();
	  return Util.createNonNullList(upperBound());
  }

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		Type lowerBound = ((Type)other).lowerBound();
		if(other instanceof WildCardType) {
			WildCardType wild = (WildCardType) other;
			return upperBound().sameAs(wild.upperBound()) && lowerBound().sameAs(wild.lowerBound());
		} else {
			return false;
		}
	}
	
	@Override
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

	@Override
   public Declaration declarator() {
		return this;
	}
	
	@Override
   public void addParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to add a parameter block to a type alias.");
	}

	@Override
   public <P extends Parameter> ParameterBlock<P> parameterBlock(Class<P> kind) {
		return null;
	}

	@Override
   public List<ParameterBlock> parameterBlocks() {
		return ImmutableList.of();
	}

	@Override
   public void removeParameterBlock(ParameterBlock block) {
		throw new ChameleonProgrammerException("Trying to remove a parameter block to a type alias.");
	}

	@Override
	public List<InheritanceRelation> implicitNonMemberInheritanceRelations() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public boolean auxSubTypeOf(Type other) throws LookupException {
		return upperBound().subTypeOf(other);
	}
	
	@Override
	public boolean auxSuperTypeOf(Type type) throws LookupException {
		return type.subTypeOf(lowerBound());
	}
}
