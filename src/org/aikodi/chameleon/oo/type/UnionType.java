package org.aikodi.chameleon.oo.type;

import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.exists;
import static be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations.forAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Pair;

import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;

/**
 * A type that is the union of a number of other types. A union type contains
 * all the values that are in at least one of its child types.
 * 
 * @author Marko van Dooren
 */
public class UnionType extends MultiType {
	
	public UnionType(List<Type> types) {
		super(createSignature(types),types);
	}
	
	@Override
	public List<Type> getProperDirectSuperTypes() throws LookupException {
		ArrayList<Type> result = new ArrayList<Type>();
		result.add(language(ObjectOrientedLanguage.class).getDefaultSuperClass(view().namespace()));
		return result;
	}

	public void addAll(UnionType type) throws LookupException {
		for(Type t: type.types()) {
			addType(t);
		}
	}
	
	public static String createSignature(Collection<Type> types) {
		StringBuffer name = new StringBuffer("union of ");
		for(Type type:types) {
			name.append(type.getFullyQualifiedName()+", ");
		}
		name.delete(name.length()-2, name.length()-1);
		return name.toString();
	}
	
	protected UnionType(String name, List<Type> types) {
		super(name,types);
	}
	
	@Override
	protected UnionType cloneSelf() {
		List<Type> types = types();
	return new UnionType(createSignature(types), types);
	}

	@Override
	public List<Declaration> localMembers() throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		return new ArrayList<Declaration>();
//		List<Member> result = new ArrayList<Member>();
//		for(Type type: types()) {
//		  result.addAll(type.localMembers(Member.class));
//		}
//		removeConstructors(result);
//		return result;
	}
	
	@Override
	public <D extends Declaration> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		return Collections.EMPTY_LIST;
		// List result = new ArrayList();
//		for(Type type: types()) {
//		  result.addAll(type.localMembers(selector));
//		}
//		removeConstructors(result);
//		return result;
	}
	
	@Override
	public LocalLookupContext<?> targetContext() throws LookupException {
		List<TypeReference> trefs = new ArrayList<>();
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		for(Type type: types()) {
			trefs.add(language.reference(type));
		}
		return language.subtypeRelation().leastUpperBound(trefs).targetContext();
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
		return result;
	}

	@Override
   public List<InheritanceRelation> nonMemberInheritanceRelations() {
		List<InheritanceRelation> result = new ArrayList<InheritanceRelation>();
		return result;
	}


 @Override
	public List<? extends TypeElement> directlyDeclaredElements() {
		List<TypeElement> result = new ArrayList<TypeElement>();
		return result;
	}
	
	

	@Override
	public boolean uniSameAs(final Element other) throws LookupException {
		List<Type> types = types();
		if (other instanceof UnionType) {
		  return forAll(types, first -> exists(((UnionType)other).types(),second -> first.sameAs(second)));
		} else {
			return (other instanceof Type) && (types.size() == 1) && (types.iterator().next().sameAs(other));
		}
	}
	
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public <P extends Parameter> P parameter(Class<P> kind, int index) {
		throw new IllegalArgumentException();
	}

	@Override
	public Type unionDoubleDispatch(Type type) throws LookupException {
		return type.unionDoubleDispatch(this);
	}

	@Override
	public Type unionDoubleDispatch(UnionType type) throws LookupException {
		UnionType result = clone(this);
		result.addAll(type);
		return type;
	}

	public void addType(Type type) throws LookupException {
		for(Type alreadyPresent:types()) {
			if(type.subtypeOf(alreadyPresent)) {
				return;
			}
			if(alreadyPresent.subtypeOf(type)) {
				removeType(alreadyPresent);
			}
		}
		// If we reach this place, then no type in the intersection is a subtype of the new type.
		_types.add(type);
	}

	@Override
   public boolean uniSameAs(final Type other, TypeFixer trace) throws LookupException {
		List<Type> types = types();
		if (other instanceof UnionType) {
		  return forAll(types,  first -> exists(((UnionType)other).types(), second -> first.sameAs(second,trace)));
		} else {
			return (types.size() == 1) && (types.iterator().next().sameAs(other,trace));
		}
	}
	
	@Override
	public boolean uniSupertypeOf(Type other,	TypeFixer trace) throws LookupException {
		int size = _types.size();
		boolean result = false;
		for(int i=0; (!result) && i<size;i++) {
			result = other.subtypeOf(_types.get(i),trace);
		}
		return result;
	}
	
	@Override
	public boolean uniSubtypeOf(Type other, TypeFixer trace) throws LookupException {
		int size = _types.size();
		boolean result = size > 0;
		for(int i=0; result && i<size;i++) {
			result = _types.get(i).subtypeOf(other, trace);
		}
		return result;
	}
}
