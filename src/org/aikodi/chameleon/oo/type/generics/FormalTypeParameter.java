package org.aikodi.chameleon.oo.type.generics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.ConstrainedTypeReference;
import org.aikodi.chameleon.oo.type.IntersectionTypeReference;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.view.ObjectOrientedView;
import org.aikodi.chameleon.util.Pair;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.association.Multi;

/**
 * This class represents generic parameters as used in e.g. Java, C#, and Eiffel.
 * 
 * @author Marko van Dooren
 */
public class FormalTypeParameter extends TypeParameter implements ElementWithTypeConstraints {

	/**
	 * Create a formal type parameter with the same name.
	 * 
	 * @param name The name of the type parameter.
	 */
	public FormalTypeParameter(String name) {
		super(name);
	}


	@Override
	protected FormalTypeParameter cloneSelf() {
		return new FormalTypeParameter(name());
	}

	/**
	 * Resolving a generic parameter results in a constructed type whose bound
	 * is the upper bound of this generic parameter as defined by the upperBound method.
	 */
	@Override
	public Type selectionDeclaration() throws LookupException {
		Type constructedType = createSelectionType();
		constructedType.setUniParent(parent());
		return constructedType;
	}


	protected Type createSelectionType() throws LookupException {
		return language().plugin(ObjectOrientedFactory.class).createTypeVariable(name(),upperBound(),this);
	}

	@Override
	public Type resolveForRoundTrip() throws LookupException {
		Type result = language().plugin(ObjectOrientedFactory.class).createLazyTypeVariable(name(), this);
		result.setUniParent(parent());
		return result;
	}

	protected final Type createLazyTypeVariable() {
		return new LazyFormalAlias(name(), this);
	}

	private Multi<TypeConstraint> _typeConstraints = new Multi<TypeConstraint>(this);
	{
		_typeConstraints.enableCache();
	}

	public List<TypeConstraint> constraints() {
		return _typeConstraints.getOtherEnds();
	}

	public int nbConstraints() {
		return _typeConstraints.size();
	}

	public void addConstraint(TypeConstraint constraint) {
		add(_typeConstraints,constraint);
	}

	@Override
	protected synchronized void flushLocalCache() {
		super.flushLocalCache();
		_typeConstraints.flushCache();
	}

	@Override
	public TypeReference upperBoundReference() {
		List<TypeConstraint> constraints = constraints();
		int size = constraints.size();
		TypeReference result;
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		if(size == 0) {
			result = language.createTypeReferenceInNamespace(language.getDefaultSuperClassFQN(), view().namespace());
		} else if(size == 1) {
			result = constraints.get(0).upperBoundReference();
		} else {
			result = language.createIntersectionReference(constraints.get(0).upperBoundReference(), constraints.get(1).upperBoundReference());
			for(int i=2; i<size;i++) {
				((IntersectionTypeReference)result).add(constraints.get(i).upperBoundReference());
			}
			result.setUniParent(this);
		}
		return result;
	}



	public FormalTypeParameter alias(SimpleNameSignature signature) {
		throw new ChameleonProgrammerException();
	}


	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		if(origin() == this) {
			if(other == other.origin()) {
				return this == other;
			} else {
				return uniSameAs(other.origin());
			}
		} else {
			return origin().sameAs(other);
		}
	}

	@Override
	public int hashCode() {
		if(origin() == this) {
			return super.hashCode();
		} else {
			return origin().hashCode();
		}

	}

	//	@Override
	//	public boolean sameValueAs(TypeParameter otherParam, TypeFixer trace) throws LookupException {
	//		return sameAs(otherParam);
	//	}

	@Override
	public String toString() {
		try {
		  return toString(new HashSet<>());
		} catch(Throwable t) {
			return "Exception occurred while computing the toString(): "+t.getClass().getName();
		}
	}

	//	public boolean contains(ActualTypeArgument other) throws LookupException {
	//		return other.upperBound().subTypeOf(upperBound()) && 
	//				lowerBound().subTypeOf(other.lowerBound());
	//	}

	@Override
	public boolean contains(TypeParameter other, TypeFixer trace) throws LookupException {
		return argument().contains(other.argument(), trace);
		//		return other.upperBound().subTypeOf(upperBound()) && 
		//				lowerBound().subTypeOf(other.lowerBound());
	}


	public TypeArgument argument() {
		if(nbConstraints() == 0) {
			ObjectOrientedLanguage oo = language(ObjectOrientedLanguage.class);
			TypeReference top = oo.createTypeReference(oo.getDefaultSuperClassFQN());
			ExtendsWildcard result = oo.createExtendsWildcard(top);
			result.setUniParent(namespace().defaultNamespace());
			return result;
		} else if (nbConstraints() == 1) {
			return constraints().get(0).argument();
		} else {
			ObjectOrientedLanguage oo = language(ObjectOrientedLanguage.class);
			ConstrainedTypeReference tref = oo.createConstrainedTypeReference();
			constraints().forEach(c -> tref.addConstraint(clone(c)));
			TypeArgument result = oo.createExtendsWildcard(tref);
			result.setUniParent(this);
			return result;
		}
	}
	
	@Override
	public String toString(Set<Element> visited) {
		if(visited.contains(this)) {
			return name();
		} else {
			visited.add(this);
			StringBuilder builder = new StringBuilder();
			builder.append(name());
			for(TypeConstraint constraint: constraints()) {
				builder.append(' ');
				builder.append(constraint.toString(visited));
			}
			return builder.toString();
		}
	}
}
