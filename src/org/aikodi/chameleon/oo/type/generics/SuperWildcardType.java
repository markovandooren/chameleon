package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;

public class SuperWildcardType extends IntervalType {

	public SuperWildcardType(Type lowerBound) throws LookupException {
		super("? super "+lowerBound.name(), lowerBound, lowerBound.language(ObjectOrientedLanguage.class).getDefaultSuperClass(lowerBound.view().namespace()));
	}
	
	
	
	protected SuperWildcardType(String name, Type lowerBound, Type upperBound) {
    super(name, lowerBound, upperBound);
  }



  public Type bound() {
		return lowerBound();
	}
	
	@Override
	protected Element cloneSelf() {
	  return new SuperWildcardType(name(), lowerBound(), upperBound());
	}
	
	@Override
	public String getFullyQualifiedName() {
		return "? super "+lowerBound().getFullyQualifiedName();
	}


}
