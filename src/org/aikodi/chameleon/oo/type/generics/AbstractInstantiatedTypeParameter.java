package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.declaration.MissingSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.Set;


public abstract class AbstractInstantiatedTypeParameter extends TypeParameter {

	public AbstractInstantiatedTypeParameter(String name, TypeArgument argument) {
		super(name);
		setArgument(argument);
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

	public TypeParameter capture(FormalTypeParameter formal) {
		return argument().capture(formal);
	}

	@Override
	public Type lowerBound() throws LookupException {
		return argument().type().lowerBound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return argument().type().upperBound();
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
	}

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
