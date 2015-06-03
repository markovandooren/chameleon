package org.aikodi.chameleon.oo.analysis.dependency;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class NoSubtypeOf extends UniversalPredicate<Type,Nothing> {

	public NoSubtypeOf(Type superType) {
		super(Type.class);
		_superType = superType;
	}
	
	private Type _superType;
	
	@Override
	public boolean uncheckedEval(Type object) {
		try {
			boolean subTypeOf = object.subtypeOf(_superType);
			return ! subTypeOf;
		} catch (LookupException e) {
			return false;
		}
	}
	
}