package chameleon.core.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.type.generics.TypeParameter;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;

public class IntersectionType extends Type {

	IntersectionType(Type first, Type second) {
		super(createSignature(Arrays.asList(new Type[]{first,second})));
		add(first);
		add(second);
	}
	
	private IntersectionType(Set<Type> types) {
		super(createSignature(types));
		_types = types;
	}

	protected Type intersectionDoubleDispatch(Type type) {
		return type.intersectionDoubleDispatch(this);
	}

	protected Type intersectionDoubleDispatch(IntersectionType type) {
		IntersectionType result = clone();
		result.addAll(type);
		return type;
	}

	public void addAll(IntersectionType type) {
		_types.addAll(type.types());
	}
	
	private Set<Type> _types = new HashSet<Type>();
	
	public void addType(Type type) {
		_types.add(type);
	}
	
	public Set<Type> types() {
		return new HashSet<Type>(_types);
	}

	
	@Override
	public void add(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add an element to an intersection type.");
	}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from an intersection type.");
	}
	@Override
	public void addInheritanceRelation(InheritanceRelation type) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to an intersection type.");
	}

  public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in an intersection type.");
  }
	
	public static SimpleNameSignature createSignature(Collection<Type> types) {
		StringBuffer name = new StringBuffer("intersection of ");
		for(Type type:types) {
			name.append(type.getFullyQualifiedName()+", ");
		}
		name.delete(name.length()-2, name.length()-1);
		return new SimpleNameSignature(name.toString());
	}
	
	@Override
	public IntersectionType clone() {
		return new IntersectionType(types());
	}

	@Override
	public List<Member> localMembers() throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		List<Member> result = new ArrayList<Member>();
		for(Type type: types()) {
		  result.addAll(type.localMembers(Member.class));
		}
		removeConstructors(result);
		return result;
	}
	
	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		List<D> result = new ArrayList<D>();
		for(Type type: types()) {
		  result.addAll(type.localMembers(selector));
		}
		removeConstructors(result);
		return result;
	}
	

	
	public void removeConstructors(List<? extends TypeElement> members) {
		Iterator<? extends TypeElement> iter = members.iterator();
		// Remove constructors. We really do need metaclasses so it seems.
		while(iter.hasNext()) {
			TypeElement member = iter.next();
			if(member.is(language(ObjectOrientedLanguage.class).CONSTRUCTOR) == Ternary.TRUE) {
				iter.remove();
			}
		}
	}

	@Override
	public List<InheritanceRelation> inheritanceRelations() {
		List<InheritanceRelation> result = new ArrayList<InheritanceRelation>();
		for(Type type: types()) {
		  result.addAll(type.inheritanceRelations());
		}
		return result;
	}

	@Override
	public void removeInheritanceRelation(InheritanceRelation type) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a intersection type.");
	}

	@Override
	public Type baseType() {
		return this;
	}

  /**
   * An intersection type has not type parameters. 
   */
	@Override
	public List<TypeParameter> parameters() {
		return new ArrayList<TypeParameter>();
	}

	@Override
	public void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a intersection type.");
	}

	@Override
	public void addParameter(TypeParameter parameter) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a intersection type.");
	}

	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		List<TypeElement> result = new ArrayList<TypeElement>();
		for(Type type: types()) {
		  result.addAll(type.directlyDeclaredElements());
		}
		removeConstructors(result);
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
