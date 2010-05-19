package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.UnsafePredicate;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.Member;
import chameleon.core.namespace.Namespace;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.generics.TypeParameter;
import chameleon.oo.type.inheritance.InheritanceRelation;
import chameleon.util.Pair;

public class UnionType extends AbstractType {
	
	public static Type create(List<Type> types) throws LookupException {
		if(types.size() == 1) {
			return types.get(0);
		} else {
			Namespace def = types.get(0).language().defaultNamespace();
			UnionType result = new UnionType(types);
			result.setUniParent(def);
			return result;
		}
	}

	public UnionType(Type first, Type second) throws LookupException {
		super(createSignature(Arrays.asList(new Type[]{first,second})));
		addType(first);
		addType(second);
	}
	
	public UnionType(List<Type> types) throws LookupException {
		super(createSignature(types));
		if(types.isEmpty()) {
			throw new ChameleonProgrammerException("Creating an intersection with an empty collection of types.");
		}
		for(Type type:types) {
			addType(type);
		}
	}
	
	@Override
	public List<Type> getDirectSuperTypes() throws LookupException {
		ArrayList<Type> result = new ArrayList<Type>();
		result.add(language(ObjectOrientedLanguage.class).getDefaultSuperClass());
		return result;
	}

	public void addAll(UnionType type) throws LookupException {
		for(Type t: type.types()) {
			addType(t);
		}
	}
	
	private List<Type> _types = new ArrayList<Type>();
	
	public void addType(Type type) throws LookupException {
		for(Type alreadyPresent:types()) {
			if(type.subTypeOf(alreadyPresent)) {
				return;
			}
			if(alreadyPresent.subTypeOf(type)) {
				removeType(alreadyPresent);
			}
		}
		// If we reach this place, then no type in the intersection is a subtype of the new type.
		_types.add(type);
	}
	
	public void removeType(Type type) {
		_types.remove(type);
	}
	
	public List<Type> types() {
		return new ArrayList<Type>(_types);
	}

	
	@Override
	public void add(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add an element to a union type.");
	}

	@Override
	public void remove(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to remove an element from a union type.");
	}
	@Override
	public void addInheritanceRelation(InheritanceRelation type) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to a union type.");
	}

  public void replace(TypeElement oldElement, TypeElement newElement) {
		throw new ChameleonProgrammerException("Trying to replace an element in a union type.");
  }
	
	public static SimpleNameSignature createSignature(Collection<Type> types) {
		StringBuffer name = new StringBuffer("union of ");
		for(Type type:types) {
			name.append(type.getFullyQualifiedName()+", ");
		}
		name.delete(name.length()-2, name.length()-1);
		return new SimpleNameSignature(name.toString());
	}
	
	private UnionType(List<Type> types, boolean useless) {
		super(createSignature(types));
		if(types.isEmpty()) {
			throw new ChameleonProgrammerException("Creating a union type with an empty collection of types.");
		}
		_types = new ArrayList<Type>(types);
	}
	
	@Override
	public UnionType clone() {
		return new UnionType(types(), false);
	}

	@Override
	public List<Member> localMembers() throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		return new ArrayList<Member>();
//		List<Member> result = new ArrayList<Member>();
//		for(Type type: types()) {
//		  result.addAll(type.localMembers(Member.class));
//		}
//		removeConstructors(result);
//		return result;
	}
	
	@Override
	public <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		List<D> result = new ArrayList<D>();
//		for(Type type: types()) {
//		  result.addAll(type.localMembers(selector));
//		}
//		removeConstructors(result);
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
//		for(Type type: types()) {
//		  result.addAll(type.inheritanceRelations());
//		}
		return result;
	}

	@Override
	public void removeInheritanceRelation(InheritanceRelation type) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a union type.");
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
	public int nbTypeParameters() {
		return 0;
	}

	@Override
	public void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		throw new ChameleonProgrammerException("Trying to replace a type parameter in a union type.");
	}

	@Override
	public void replaceAllParameter(List<TypeParameter> newParameters) {
		throw new ChameleonProgrammerException("Trying to replace type parameters in a union type.");
	}

	@Override
	public void addParameter(TypeParameter parameter) {
		throw new ChameleonProgrammerException("Trying to add a type parameter to a union type.");
	}

	@Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		List<TypeElement> result = new ArrayList<TypeElement>();
//		for(Type type: types()) {
//		  result.addAll(type.directlyDeclaredElements());
//		}
//		removeConstructors(result);
		return result;
	}
	
	

	@Override
	public boolean uniSameAs(final Element other) throws LookupException {
		List<Type> types = types();
		if (other instanceof UnionType) {
			return new UnsafePredicate<Type, LookupException>() {
				@Override
				public boolean eval(final Type first) throws LookupException {
					return new UnsafePredicate<Type, LookupException>() {
						@Override
						public boolean eval(Type second) throws LookupException {
							return first.sameAs(second);
						}
						
					}.exists(((UnionType)other).types());
				}
			}.forAll(types);
		} else {
			return (other instanceof Type) && (types.size() == 1) && (types.iterator().next().sameAs(other));
		}
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		for(Type type:types()) {
			result += type.hashCode();
		}
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	@Override
	public TypeParameter parameter(int index) {
		throw new IllegalArgumentException();
	}

	@Override
	public Type unionDoubleDispatch(Type type) throws LookupException {
		return type.unionDoubleDispatch(this);
	}

	@Override
	public Type unionDoubleDispatch(UnionType type) throws LookupException {
		UnionType result = clone();
		result.addAll(type);
		return type;
	}

	public boolean uniSameAs(final Type other, final List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		List<Type> types = types();
		if (other instanceof UnionType) {
			return new UnsafePredicate<Type, LookupException>() {
				@Override
				public boolean eval(final Type first) throws LookupException {
					return new UnsafePredicate<Type, LookupException>() {
						@Override
						public boolean eval(Type second) throws LookupException {
							return first.sameAs(second,trace);
						}
						
					}.exists(((UnionType)other).types());
				}
			}.forAll(types);
		} else {
			return (types.size() == 1) && (types.iterator().next().sameAs(other,trace));
		}
	}

}
