package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;

public class ConstrainedType extends IntervalType {

	public ConstrainedType(Type lowerBound, Type upperBound) {
		super("? super "+lowerBound.name()+" extends "+upperBound.name(), lowerBound, upperBound);
	}

	@Override
	protected ConstrainedType cloneSelf() {
		return new ConstrainedType(lowerBound(), upperBound());
	}

	@Override
	public String getFullyQualifiedName() {
		return "? super "+lowerBound().getFullyQualifiedName()+" extends "+upperBound().getFullyQualifiedName();
	}

	@Override
	public boolean properSubTypeOf(Type other) throws LookupException {
		return upperBound().subTypeOf(other);
	}
	
	@Override
	public boolean properSuperTypeOf(Type type) throws LookupException {
		throw new Error("Wrong");
	}
	
	/* (non-Javadoc)
	 * @see org.aikodi.chameleon.oo.type.generics.IntervalType#upperBoundNotHigherThan(org.aikodi.chameleon.oo.type.Type, org.aikodi.chameleon.oo.type.TypeFixer)
	 */
	@Override
	public boolean upperBoundNotHigherThan(Type other, TypeFixer trace) throws LookupException {
	  return upperBound().upperBoundNotHigherThan(other, trace);
	}
	
//	/* (non-Javadoc)
//	 * @see org.aikodi.chameleon.oo.type.generics.IntervalType#upperBoundAtLeastAsHighAs(org.aikodi.chameleon.oo.type.Type, org.aikodi.chameleon.oo.type.TypeFixer)
//	 */
//	@Override
//	public boolean upperBoundAtLeastAsHighAs(Type other, TypeFixer trace) throws LookupException {
//		return upperBound().upperBoundAtLeastAsHighAs(other, trace);
//	}

}
