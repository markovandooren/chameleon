package org.aikodi.chameleon.oo.type.generics;

import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.MissingSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;


public abstract class AbstractInstantiatedTypeParameter extends TypeParameter {

	public AbstractInstantiatedTypeParameter(String name, TypeArgument argument) {
		super(name);
		setArgument(argument);
	}

	/**
	 * Return a substitution map that specifies which substitutions must be done in the given
	 * element if this type parameter were to be substituted.
	 * @param element The element in which the substitution is to be done.
	 * @return
	 * @throws LookupException
	 */
	public TypeParameterSubstitution substitution(Element element) throws LookupException {
		List<CrossReference> crossReferences = 
				element.descendants(CrossReference.class, object -> object.getDeclarator().sameAs(AbstractInstantiatedTypeParameter.this));

		return new TypeParameterSubstitution(this, crossReferences);
	}

	private void setArgument(TypeArgument type) {
		_argument = type;
	}

	public TypeArgument argument() {
		return _argument;
	}

	private TypeArgument _argument;

	@Override
	public Type selectionDeclaration() throws LookupException {
		if(_selectionTypeCache == null) {
			synchronized(this) {
				if(_selectionTypeCache == null) {
					ObjectOrientedFactory plugin = language().plugin(ObjectOrientedFactory.class);
					_selectionTypeCache = plugin.createInstantiatedTypeVariable(name(),argument().type(),this);
				}
			}
		}
		return _selectionTypeCache;
	}

	@Override
	public synchronized void flushLocalCache() {
		super.flushLocalCache();
		_selectionTypeCache = null;
	}

	private Type _selectionTypeCache;


	@Override
	public Type resolveForRoundTrip() throws LookupException {
		Type result = language().plugin(ObjectOrientedFactory.class).createLazyInstantiatedTypeVariable(name(),this);
		result.setUniParent(parent());
		return result;
	}

	public TypeParameter capture(FormalTypeParameter formal, List<TypeConstraint> accumulator) {
		return argument().capture(formal,accumulator);
	}

	@Override
	public Type lowerBound() throws LookupException {
		return argument().type().lowerBound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return argument().type().upperBound();
	}

	public Type actualLowerBound() throws LookupException {
		return lowerBound();
	}
	
	public Type actualUpperBound() throws LookupException {
		return upperBound();
	}

	@Override
	public Verification verifySelf() {
		Verification tmp = super.verifySelf();
		if(argument() != null) {
			return tmp;
		} else {
			return tmp.and(new MissingSignature(this)); 
		}
	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		return other == this;
		//		boolean result = false;
		//		if(other instanceof InstantiatedTypeParameter) {
		//		 result = argument().sameAs(((InstantiatedTypeParameter)other).argument());
		//		}
		//		return result;
	}

	//	@Override
	//	public boolean sameValueAs(TypeParameter other) throws LookupException {
	//		boolean result = false;
	//		if(other instanceof AbstractInstantiatedTypeParameter) {
	//			result = argument().sameAs(((InstantiatedTypeParameter)other).argument());
	//		}
	//		return result;
	//	}

//	@Override
//	public boolean sameValueAs(TypeParameter other, TypeFixer trace) throws LookupException {
//		boolean result = false;
//		if(other instanceof AbstractInstantiatedTypeParameter) {
//			result = argument().sameAs(((InstantiatedTypeParameter)other).argument(), trace);
//		}
//		return result;
//	}

	@Override
	public int hashCode() {
		return argument().hashCode();
	}

	@Override
	public TypeReference upperBoundReference() throws LookupException {
		return argument().substitutionReference();
	}

	@Override
	public String toString() {
		return argument().toString();
	}
	
	@Override
	public boolean contains(TypeParameter other, TypeFixer trace) throws LookupException {
//		return argument().contains(other.argument(), trace);
		return argument().contains(other, trace); // try to avoid early unroll
	}
	
	/* (non-Javadoc)
	 * @see org.aikodi.chameleon.oo.type.generics.TypeParameter#toString(java.util.Set)
	 */
	@Override
	public String toString(Set<Element> visited) {
		if(visited.contains(this)) {
			return name();
		} else {
			visited.add(this);
			return argument().toString(visited);
		}
	}
}
